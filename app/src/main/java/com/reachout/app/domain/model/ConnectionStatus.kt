package com.reachout.app.domain.model

/**
 * Represents the current network connectivity status for offline-first operations.
 */
sealed class ConnectionStatus {
    /**
     * Network connectivity is actively available and operational.
     */
    data object Available : ConnectionStatus()

    /**
     * Network connectivity is currently unavailable.
     */
    data object Unavailable : ConnectionStatus()

    /**
     * Network connection quality is degrading and may be lost soon.
     */
    data object Losing : ConnectionStatus()

    /**
     * Network connection has been disconnected or lost.
     */
    data object Lost : ConnectionStatus()
}
