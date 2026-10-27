package com.reachout.app.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reachout.app.R
import com.reachout.app.presentation.navigation.Screen
import com.reachout.app.presentation.theme.ReachOutTheme
import com.reachout.app.presentation.theme.CategoryAgriculture
import com.reachout.app.presentation.theme.CategoryAgricultureBg
import com.reachout.app.presentation.theme.CategoryBreakdown
import com.reachout.app.presentation.theme.CategoryBreakdownBg
import com.reachout.app.presentation.theme.CategoryCommunity
import com.reachout.app.presentation.theme.CategoryCommunityBg
import com.reachout.app.presentation.theme.CategoryDirectory
import com.reachout.app.presentation.theme.CategoryDirectoryBg
import com.reachout.app.presentation.theme.CategoryEmergency
import com.reachout.app.presentation.theme.CategoryLanguage
import com.reachout.app.presentation.theme.CategoryLanguageBg
import com.reachout.app.presentation.theme.CategoryMap
import com.reachout.app.presentation.theme.CategoryMapBg
import com.reachout.app.presentation.theme.CategoryMedical
import com.reachout.app.presentation.theme.CategoryMedicalBg
import com.reachout.app.presentation.theme.CategoryRequests
import com.reachout.app.presentation.theme.CategoryRequestsBg
import com.reachout.app.presentation.theme.CategorySaved
import com.reachout.app.presentation.theme.CategorySavedBg
import com.reachout.app.presentation.theme.CategorySettings
import com.reachout.app.presentation.theme.CategorySettingsBg
import com.reachout.app.presentation.theme.CategoryTransport
import com.reachout.app.presentation.theme.CategoryTransportBg

/**
 * Service category item representation for the home grid.
 */
private data class ServiceCategory(
    val titleRes: Int,
    val icon: ImageVector,
    val route: String,
    val iconColor: Color,
    val iconBgColor: Color,
    val isEmergency: Boolean = false,
    val subtitleRes: Int? = null
)

/**
 * ReachOut main dashboard screen providing quick access to essential community
 * assistance categories, offline services, and emergency help.
 *
 * @param navController Navigation controller to handle screen transitions.
 * @param modifier Optional layout modifier.
 */
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        ServiceCategory(
            titleRes = R.string.home_emergency_help,
            icon = Icons.Filled.Emergency,
            route = Screen.EmergencyHelp.route,
            iconColor = Color.White,
            iconBgColor = Color(0x33FFFFFF),
            isEmergency = true,
            subtitleRes = R.string.home_emergency_subtitle
        ),
        ServiceCategory(
            titleRes = R.string.home_medical_help,
            icon = Icons.Filled.LocalHospital,
            route = Screen.MedicalHelp.route,
            iconColor = CategoryMedical,
            iconBgColor = CategoryMedicalBg
        ),
        ServiceCategory(
            titleRes = R.string.home_transport_help,
            icon = Icons.Filled.DirectionsBus,
            route = Screen.TransportHelp.route,
            iconColor = CategoryTransport,
            iconBgColor = CategoryTransportBg
        ),
        ServiceCategory(
            titleRes = R.string.home_agricultural_help,
            icon = Icons.Filled.Agriculture,
            route = Screen.AgriculturalHelp.route,
            iconColor = CategoryAgriculture,
            iconBgColor = CategoryAgricultureBg
        ),
        ServiceCategory(
            titleRes = R.string.home_vehicle_breakdown,
            icon = Icons.Filled.CarRepair,
            route = Screen.VehicleBreakdown.route,
            iconColor = CategoryBreakdown,
            iconBgColor = CategoryBreakdownBg
        ),
        ServiceCategory(
            titleRes = R.string.home_community_support,
            icon = Icons.Filled.Groups,
            route = Screen.CommunitySupport.route,
            iconColor = CategoryCommunity,
            iconBgColor = CategoryCommunityBg
        ),
        ServiceCategory(
            titleRes = R.string.home_contact_directory,
            icon = Icons.Filled.Contacts,
            route = Screen.ContactDirectory.route,
            iconColor = CategoryDirectory,
            iconBgColor = CategoryDirectoryBg
        ),
        ServiceCategory(
            titleRes = R.string.home_safe_places_map,
            icon = Icons.Filled.Map,
            route = Screen.SafePlacesMap.route,
            iconColor = CategoryMap,
            iconBgColor = CategoryMapBg
        ),
        ServiceCategory(
            titleRes = R.string.home_saved_offline_info,
            icon = Icons.Filled.OfflinePin,
            route = Screen.SavedInfo.route,
            iconColor = CategorySaved,
            iconBgColor = CategorySavedBg
        ),
        ServiceCategory(
            titleRes = R.string.home_my_requests,
            icon = Icons.AutoMirrored.Filled.Assignment,
            route = Screen.MyRequests.route,
            iconColor = CategoryRequests,
            iconBgColor = CategoryRequestsBg
        ),
        ServiceCategory(
            titleRes = R.string.home_language_settings,
            icon = Icons.Filled.Language,
            route = Screen.LanguageSettings.route,
            iconColor = CategoryLanguage,
            iconBgColor = CategoryLanguageBg
        ),
        ServiceCategory(
            titleRes = R.string.home_settings,
            icon = Icons.Filled.Settings,
            route = Screen.Settings.route,
            iconColor = CategorySettings,
            iconBgColor = CategorySettingsBg
        )
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Screen Header
            item(span = { GridItemSpan(2) }) {
                HomeHeader()
            }

            // Category Grid Items
            items(
                items = categories,
                span = { item -> GridItemSpan(if (item.isEmergency) 2 else 1) }
            ) { category ->
                if (category.isEmergency) {
                    EmergencyCategoryCard(
                        category = category,
                        onClick = { navController.navigate(category.route) }
                    )
                } else {
                    StandardCategoryCard(
                        category = category,
                        onClick = { navController.navigate(category.route) }
                    )
                }
            }
        }
    }
}

/**
 * Home screen header with app title, subtitle, and safety badge.
 */
@Composable
private fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.app_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.safety_warning_otp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Prominent full-width Emergency Help card styled with urgent red accent.
 */
@Composable
private fun EmergencyCategoryCard(
    category: ServiceCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = CategoryEmergency
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SOS",
                            style = MaterialTheme.typography.labelSmall,
                            color = CategoryEmergency,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(category.titleRes),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                category.subtitleRes?.let { subRes ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(subRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

/**
 * Standard category card for assistance services.
 */
@Composable
private fun StandardCategoryCard(
    category: ServiceCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(category.iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = category.iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(category.titleRes),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ==========================================
// Previews for Android Studio Layout Preview
// ==========================================

@Preview(name = "Home Screen - Light Mode", showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    ReachOutTheme(darkTheme = false) {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(name = "Home Screen - Dark Mode", showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenDarkPreview() {
    ReachOutTheme(darkTheme = true) {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(name = "Emergency Card Preview", showBackground = true)
@Composable
fun EmergencyCardPreview() {
    ReachOutTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            EmergencyCategoryCard(
                category = ServiceCategory(
                    titleRes = R.string.home_emergency_help,
                    icon = Icons.Filled.Emergency,
                    route = Screen.EmergencyHelp.route,
                    iconColor = Color.White,
                    iconBgColor = Color(0x33FFFFFF),
                    isEmergency = true,
                    subtitleRes = R.string.home_emergency_subtitle
                ),
                onClick = {}
            )
        }
    }
}

@Preview(name = "Standard Card Preview", showBackground = true)
@Composable
fun StandardCardPreview() {
    ReachOutTheme {
        Box(modifier = Modifier.padding(16.dp).size(160.dp, 130.dp)) {
            StandardCategoryCard(
                category = ServiceCategory(
                    titleRes = R.string.home_medical_help,
                    icon = Icons.Filled.LocalHospital,
                    route = Screen.MedicalHelp.route,
                    iconColor = CategoryMedical,
                    iconBgColor = CategoryMedicalBg
                ),
                onClick = {}
            )
        }
    }
}
