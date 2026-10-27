package com.reachout.app

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.reachout.app.domain.model.ConnectionStatus
import com.reachout.app.presentation.navigation.ReachOutApp
import com.reachout.app.presentation.navigation.ReachOutAppContent
import com.reachout.app.presentation.theme.ReachOutTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main launcher [ComponentActivity] hosting the ReachOut Jetpack Compose user interface.
 *
 * Configured with [@AndroidEntryPoint] for Hilt dependency injection, edge-to-edge window insets,
 * and the root application theme and navigation graph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReachOutTheme {
                ReachOutApp()
            }
        }
    }
}

// ==========================================
// Previews for Android Studio Layout Preview
// ==========================================

@Preview(name = "Main Activity - Full App (Light)", showBackground = true, showSystemUi = true)
@Composable
fun MainActivityPreview() {
    ReachOutTheme(darkTheme = false) {
        ReachOutAppContent(connectionStatus = ConnectionStatus.Available)
    }
}

@Preview(name = "Main Activity - Offline Mode", showBackground = true, showSystemUi = true)
@Composable
fun MainActivityOfflinePreview() {
    ReachOutTheme(darkTheme = false) {
        ReachOutAppContent(connectionStatus = ConnectionStatus.Unavailable)
    }
}

@Preview(name = "Main Activity - Full App (Dark)", showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainActivityDarkPreview() {
    ReachOutTheme(darkTheme = true) {
        ReachOutAppContent(connectionStatus = ConnectionStatus.Available)
    }
}
