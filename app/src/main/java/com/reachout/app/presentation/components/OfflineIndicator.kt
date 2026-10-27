package com.reachout.app.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reachout.app.R
import com.reachout.app.domain.model.ConnectionStatus
import com.reachout.app.presentation.theme.ReachOutTheme
import com.reachout.app.presentation.theme.OfflineBannerBackground
import com.reachout.app.presentation.theme.OfflineBannerBackgroundDark
import com.reachout.app.presentation.theme.OfflineBannerText
import com.reachout.app.presentation.theme.OfflineBannerTextDark
import com.reachout.app.presentation.theme.OnlineBannerBackground
import com.reachout.app.presentation.theme.OnlineBannerBackgroundDark
import com.reachout.app.presentation.theme.OnlineBannerText
import com.reachout.app.presentation.theme.OnlineBannerTextDark
import com.reachout.app.presentation.theme.UnstableBannerBackground
import com.reachout.app.presentation.theme.UnstableBannerBackgroundDark
import com.reachout.app.presentation.theme.UnstableBannerText
import com.reachout.app.presentation.theme.UnstableBannerTextDark
import kotlinx.coroutines.delay

/**
 * Visual indicator banner displaying network connectivity status.
 *
 * - When [ConnectionStatus.Unavailable] or [ConnectionStatus.Lost]: Shows an amber warning
 *   banner with offline notice.
 * - When [ConnectionStatus.Losing]: Shows a caution banner for unstable connection.
 * - When transitioning to [ConnectionStatus.Available]: Briefly displays a green "Back online"
 *   badge before smoothly fading out.
 *
 * @param connectionStatus Current connectivity state of the application.
 * @param modifier Optional modifier for the indicator layout.
 */
@Composable
fun OfflineIndicator(
    connectionStatus: ConnectionStatus,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    var showBackOnline by remember { mutableStateOf(false) }
    var previousStatus by remember { mutableStateOf<ConnectionStatus?>(null) }

    LaunchedEffect(connectionStatus) {
        if (previousStatus != null &&
            previousStatus != ConnectionStatus.Available &&
            connectionStatus == ConnectionStatus.Available
        ) {
            showBackOnline = true
            delay(3000L)
            showBackOnline = false
        }
        previousStatus = connectionStatus
    }

    val isVisible = connectionStatus != ConnectionStatus.Available || showBackOnline

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        val bannerConfig = when {
            showBackOnline -> {
                BannerConfig(
                    backgroundColor = if (isDark) OnlineBannerBackgroundDark else OnlineBannerBackground,
                    textColor = if (isDark) OnlineBannerTextDark else OnlineBannerText,
                    icon = Icons.Filled.CheckCircle,
                    messageRes = R.string.offline_indicator_back_online
                )
            }
            connectionStatus == ConnectionStatus.Losing -> {
                BannerConfig(
                    backgroundColor = if (isDark) UnstableBannerBackgroundDark else UnstableBannerBackground,
                    textColor = if (isDark) UnstableBannerTextDark else UnstableBannerText,
                    icon = Icons.Filled.Warning,
                    messageRes = R.string.offline_indicator_connection_unstable
                )
            }
            else -> {
                BannerConfig(
                    backgroundColor = if (isDark) OfflineBannerBackgroundDark else OfflineBannerBackground,
                    textColor = if (isDark) OfflineBannerTextDark else OfflineBannerText,
                    icon = Icons.Filled.WifiOff,
                    messageRes = R.string.offline_indicator_no_connection
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(bannerConfig.backgroundColor)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = bannerConfig.icon,
                    contentDescription = null,
                    tint = bannerConfig.textColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = bannerConfig.messageRes),
                    color = bannerConfig.textColor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Internal banner configuration holder.
 */
private data class BannerConfig(
    val backgroundColor: Color,
    val textColor: Color,
    val icon: ImageVector,
    val messageRes: Int
)

/**
 * Full-width top banner for displaying network connectivity status across screens.
 *
 * @param connectionStatus Current connectivity state.
 * @param modifier Optional layout modifier.
 */
@Composable
fun ConnectionStatusBanner(
    connectionStatus: ConnectionStatus,
    modifier: Modifier = Modifier
) {
    OfflineIndicator(
        connectionStatus = connectionStatus,
        modifier = modifier.fillMaxWidth()
    )
}

// ==========================================
// Previews for Android Studio Layout Preview
// ==========================================

@Preview(name = "Offline Indicator - Disconnected", showBackground = true)
@Composable
fun ConnectionStatusBannerOfflinePreview() {
    ReachOutTheme {
        ConnectionStatusBanner(connectionStatus = ConnectionStatus.Unavailable)
    }
}

@Preview(name = "Offline Indicator - Unstable", showBackground = true)
@Composable
fun ConnectionStatusBannerUnstablePreview() {
    ReachOutTheme {
        ConnectionStatusBanner(connectionStatus = ConnectionStatus.Losing)
    }
}

@Preview(name = "Offline Indicator - Back Online", showBackground = true)
@Composable
fun ConnectionStatusBannerOnlinePreview() {
    ReachOutTheme {
        ConnectionStatusBanner(connectionStatus = ConnectionStatus.Available)
    }
}
