package com.reachout.app.presentation.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reachout.app.R
import com.reachout.app.data.connectivity.ConnectivityObserver
import com.reachout.app.domain.model.ConnectionStatus
import com.reachout.app.presentation.components.ConnectionStatusBanner
import com.reachout.app.presentation.directory.ContactDirectoryScreen
import com.reachout.app.presentation.emergency.EmergencyHotlinesScreen
import com.reachout.app.presentation.home.HomeScreen
import com.reachout.app.presentation.places.SafePlacesScreen
import com.reachout.app.presentation.requests.MyRequestsScreen
import com.reachout.app.presentation.saved.SavedInfoScreen
import com.reachout.app.presentation.settings.SettingsScreen
import com.reachout.app.presentation.theme.ReachOutTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Screen navigation routes for the ReachOut application.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object MedicalHelp : Screen("medical_help")
    data object TransportHelp : Screen("transport_help")
    data object AgriculturalHelp : Screen("agricultural_help")
    data object VehicleBreakdown : Screen("vehicle_breakdown")
    data object CommunitySupport : Screen("community_support")
    data object ContactDirectory : Screen("contact_directory")
    data object SafePlacesMap : Screen("safe_places_map")
    data object EmergencyHelp : Screen("emergency_help")
    data object SavedInfo : Screen("saved_info")
    data object MyRequests : Screen("my_requests")
    data object LanguageSettings : Screen("language_settings")
    data object Settings : Screen("settings")
}

/**
 * ViewModel managing global application state including network connectivity monitoring.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val connectionStatus: StateFlow<ConnectionStatus> = connectivityObserver.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectionStatus.Available
        )
}

/**
 * Temporary placeholder screen shown for upcoming modules.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(
    title: String,
    onBackClick: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.action_back)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.coming_soon),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.placeholder_screen_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Main application composable with Scaffold, connectivity banner, and navigation host.
 */
@Composable
fun ReachOutApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val connectionStatus by viewModel.connectionStatus.collectAsStateWithLifecycle()
    ReachOutAppContent(connectionStatus = connectionStatus)
}

/**
 * Main layout content separating ViewModel dependency for seamless Android Studio Previews.
 */
@Composable
fun ReachOutAppContent(
    connectionStatus: ConnectionStatus = ConnectionStatus.Available
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            ConnectionStatusBanner(connectionStatus = connectionStatus)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.ContactDirectory.route) {
                ContactDirectoryScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.SafePlacesMap.route) {
                SafePlacesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.EmergencyHelp.route) {
                EmergencyHotlinesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.MedicalHelp.route) {
                SafePlacesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.TransportHelp.route) {
                SafePlacesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.VehicleBreakdown.route) {
                SafePlacesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.AgriculturalHelp.route) {
                SafePlacesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.CommunitySupport.route) {
                ContactDirectoryScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.SavedInfo.route) {
                SavedInfoScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.MyRequests.route) {
                MyRequestsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.LanguageSettings.route) {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

// ==========================================
// Previews for Android Studio Layout Preview
// ==========================================

@Preview(name = "1. Full App - Online (Light)", showBackground = true, showSystemUi = true)
@Composable
fun ReachOutAppOnlinePreview() {
    ReachOutTheme(darkTheme = false) {
        ReachOutAppContent(connectionStatus = ConnectionStatus.Available)
    }
}

@Preview(name = "2. Full App - Offline Mode Banner", showBackground = true, showSystemUi = true)
@Composable
fun ReachOutAppOfflinePreview() {
    ReachOutTheme(darkTheme = false) {
        ReachOutAppContent(connectionStatus = ConnectionStatus.Unavailable)
    }
}

@Preview(name = "3. Full App - Dark Mode", showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReachOutAppDarkPreview() {
    ReachOutTheme(darkTheme = true) {
        ReachOutAppContent(connectionStatus = ConnectionStatus.Available)
    }
}

@Preview(name = "4. Screen Preview - Emergency Hotlines", showBackground = true, showSystemUi = true)
@Composable
fun EmergencyHotlinesNavPreview() {
    ReachOutTheme {
        EmergencyHotlinesScreen(onBackClick = {})
    }
}

@Preview(name = "5. Screen Preview - Safe Places Map & List", showBackground = true, showSystemUi = true)
@Composable
fun SafePlacesNavPreview() {
    ReachOutTheme {
        SafePlacesScreen(onBackClick = {})
    }
}

@Preview(name = "6. Screen Preview - Contact Directory", showBackground = true, showSystemUi = true)
@Composable
fun ContactDirectoryNavPreview() {
    ReachOutTheme {
        ContactDirectoryScreen(onBackClick = {})
    }
}

@Preview(name = "7. Screen Preview - My Requests & Firebase Status", showBackground = true, showSystemUi = true)
@Composable
fun MyRequestsNavPreview() {
    ReachOutTheme {
        MyRequestsScreen(onBackClick = {})
    }
}

@Preview(name = "8. Screen Preview - Saved Offline Info", showBackground = true, showSystemUi = true)
@Composable
fun SavedInfoNavPreview() {
    ReachOutTheme {
        SavedInfoScreen(onBackClick = {})
    }
}

@Preview(name = "9. Screen Preview - Settings & Language", showBackground = true, showSystemUi = true)
@Composable
fun SettingsNavPreview() {
    ReachOutTheme {
        SettingsScreen(onBackClick = {})
    }
}
