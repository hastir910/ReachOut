package com.reachout.app.presentation.places

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reachout.app.data.local.entity.PlaceEntity
import com.reachout.app.data.local.seed.InitialDataset
import com.reachout.app.presentation.theme.ReachOutTheme
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafePlacesScreen(
    onBackClick: () -> Unit,
    viewModel: SafePlacesViewModel? = if (androidx.compose.ui.platform.LocalInspectionMode.current) null else hiltViewModel()
) {
    if (viewModel == null) {
        SafePlacesContent(
            places = InitialDataset.safePlaces,
            searchQuery = "",
            selectedCategory = "ALL",
            showSavedOnly = false,
            onBackClick = onBackClick,
            onSearchQueryChange = {},
            onCategorySelect = {},
            onToggleSavedOnly = {},
            onBookmarkToggle = {},
            onDial = {},
            onRequestHelpClick = {}
        )
        return
    }

    val places by viewModel.places.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val showSavedOnly by viewModel.showSavedOnly.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showRequestDialog by remember { mutableStateOf(false) }

    SafePlacesContent(
        places = places,
        searchQuery = searchQuery,
        selectedCategory = selectedCategory,
        showSavedOnly = showSavedOnly,
        onBackClick = onBackClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onCategorySelect = viewModel::onCategorySelect,
        onToggleSavedOnly = viewModel::toggleSavedOnly,
        onBookmarkToggle = viewModel::toggleBookmark,
        onDial = { number ->
            if (number.isNotBlank()) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                context.startActivity(intent)
            }
        },
        onRequestHelpClick = { showRequestDialog = true }
    )

    if (showRequestDialog) {
        CustomerHelpDialog(
            onDismiss = { showRequestDialog = false },
            onSubmit = { name, number, type, desc ->
                viewModel.submitHelpRequest(name, number, type, desc) { synced ->
                    showRequestDialog = false
                    scope.launch {
                        if (synced) {
                            snackbarHostState.showSnackbar("Request saved locally & synced to Firebase!")
                        } else {
                            snackbarHostState.showSnackbar("Saved locally offline. Will sync when online.")
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafePlacesContent(
    places: List<PlaceEntity>,
    searchQuery: String,
    selectedCategory: String,
    showSavedOnly: Boolean,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onToggleSavedOnly: () -> Unit,
    onBookmarkToggle: (PlaceEntity) -> Unit,
    onDial: (String) -> Unit,
    onRequestHelpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: List, 1: Map
    val categories = listOf("ALL", "HOSPITAL", "CLINIC", "PHARMACY", "POLICE_STATION", "SHELTER", "VEHICLE_BREAKDOWN", "COMMUNITY_CENTRE")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Safe Places & Facilities", fontWeight = FontWeight.Bold)
                        Text(
                            "${places.size} locations available offline",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleSavedOnly) {
                        Icon(
                            imageVector = if (showSavedOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Saved places",
                            tint = if (showSavedOnly) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onRequestHelpClick,
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Request Help", fontWeight = FontWeight.Bold)
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // View Mode Tab (List View vs Map View)
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("List View")
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Map View")
                        }
                    }
                )
            }

            // Search input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search hospital, police, area, landmark...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Category Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val label = when (category) {
                        "ALL" -> "All Places"
                        "HOSPITAL" -> "Hospitals"
                        "CLINIC" -> "Clinics"
                        "PHARMACY" -> "Pharmacies"
                        "POLICE_STATION" -> "Police"
                        "SHELTER" -> "Shelters"
                        "VEHICLE_BREAKDOWN" -> "Mechanics"
                        "COMMUNITY_CENTRE" -> "Agro & Community"
                        else -> category
                    }
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategorySelect(category) },
                        label = { Text(label) }
                    )
                }
            }

            if (selectedTab == 0) {
                // List View
                if (places.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No places found matching your filter.",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Try searching for a different area or clear the filter.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(places, key = { it.id }) { place ->
                            PlaceItemCard(
                                place = place,
                                onBookmarkToggle = { onBookmarkToggle(place) },
                                onDial = { onDial(place.contactNumber ?: "") }
                            )
                        }
                        item {
                            Spacer(Modifier.height(80.dp))
                        }
                    }
                }
            } else {
                // Map View
                Box(modifier = Modifier.fillMaxSize()) {
                    OsmMapView(
                        places = places,
                        onPlaceClick = { place ->
                            // Tapping place marker
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceItemCard(
    place: PlaceEntity,
    onBookmarkToggle: () -> Unit,
    onDial: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = place.placeType.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(onClick = onBookmarkToggle) {
                    Icon(
                        imageVector = if (place.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save place",
                        tint = if (place.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (!place.address.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${place.address}${if (place.landmark != null) " (${place.landmark})" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (place.latitude != null && place.longitude != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "GPS: ${String.format("%.4f", place.latitude)}, ${String.format("%.4f", place.longitude)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (!place.servicesAvailable.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Services: ${place.servicesAvailable}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (place.isSampleData) {
                        Text(
                            text = "[Sample Data]",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = place.operatingHours ?: "Hours not verified",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (!place.contactNumber.isNullOrBlank()) {
                    Button(
                        onClick = onDial,
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Call", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

/**
 * OpenStreetMap (osmdroid) MapView integrated via AndroidView.
 */
@Composable
fun OsmMapView(
    places: List<PlaceEntity>,
    onPlaceClick: (PlaceEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (androidx.compose.ui.platform.LocalInspectionMode.current) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    Icons.Default.Map,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "OpenStreetMap Offline Engine",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Interactive Map with ${places.size} test facilities centered at 28.6250, 77.2150",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        return
    }

    val context = LocalContext.current
    var selectedPlace by remember { mutableStateOf<PlaceEntity?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(13.0)
                    // Default center around test dataset locations
                    val centerPoint = GeoPoint(28.6250, 77.2150)
                    controller.setCenter(centerPoint)
                }
            },
            update = { mapView ->
                mapView.overlays.clear()
                places.forEach { place ->
                    if (place.latitude != null && place.longitude != null) {
                        val marker = Marker(mapView)
                        marker.position = GeoPoint(place.latitude, place.longitude)
                        marker.title = place.name
                        marker.snippet = "${place.placeType}\n${place.contactNumber ?: ""}"
                        marker.setOnMarkerClickListener { _, _ ->
                            selectedPlace = place
                            onPlaceClick(place)
                            true
                        }
                        mapView.overlays.add(marker)
                    }
                }
                mapView.invalidate()
            },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay card showing details of clicked marker
        selectedPlace?.let { place ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(place.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { selectedPlace = null }) {
                            Icon(Icons.Default.Clear, contentDescription = "Close")
                        }
                    }
                    Text(
                        place.address ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (!place.contactNumber.isNullOrBlank()) {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${place.contactNumber}"))
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Call ${place.contactNumber ?: ""}")
                    }
                }
            }
        }
    }
}

/**
 * Dialog enabling customer to enter their phone number and help request.
 */
@Composable
fun CustomerHelpDialog(
    onDismiss: () -> Unit,
    onSubmit: (name: String, number: String, type: String, description: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var assistanceType by remember { mutableStateOf("MEDICAL") }
    var description by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Request Assistance", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Enter your callback number so community responders or services can reach out to you immediately.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = number,
                    onValueChange = {
                        number = it
                        hasError = false
                    },
                    label = { Text("Your Phone Number *") },
                    placeholder = { Text("+91 98765 43210") },
                    singleLine = true,
                    isError = hasError,
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (hasError) {
                    Text("Please provide a contact number", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Describe Help Needed") },
                    placeholder = { Text("e.g. Ambulance needed, vehicle flat tyre, medical supplies...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (number.isBlank()) {
                        hasError = true
                    } else {
                        onSubmit(name, number, assistanceType, description)
                    }
                }
            ) {
                Text("Submit Request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ==================== COMPOSE PREVIEWS ====================

@Preview(name = "Safe Places Light", showBackground = true)
@Composable
fun SafePlacesPreview() {
    ReachOutTheme {
        SafePlacesContent(
            places = InitialDataset.safePlaces,
            searchQuery = "",
            selectedCategory = "ALL",
            showSavedOnly = false,
            onBackClick = {},
            onSearchQueryChange = {},
            onCategorySelect = {},
            onToggleSavedOnly = {},
            onBookmarkToggle = {},
            onDial = {},
            onRequestHelpClick = {}
        )
    }
}

@Preview(name = "Safe Places Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SafePlacesDarkPreview() {
    ReachOutTheme {
        SafePlacesContent(
            places = InitialDataset.safePlaces,
            searchQuery = "",
            selectedCategory = "ALL",
            showSavedOnly = false,
            onBackClick = {},
            onSearchQueryChange = {},
            onCategorySelect = {},
            onToggleSavedOnly = {},
            onBookmarkToggle = {},
            onDial = {},
            onRequestHelpClick = {}
        )
    }
}

@Preview(name = "Customer Help Dialog", showBackground = true)
@Composable
fun CustomerHelpDialogPreview() {
    ReachOutTheme {
        CustomerHelpDialog(onDismiss = {}, onSubmit = { _, _, _, _ -> })
    }
}
