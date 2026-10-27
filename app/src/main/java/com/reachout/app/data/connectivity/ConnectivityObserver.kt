package com.reachout.app.data.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.reachout.app.domain.model.ConnectionStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Interface defining contract for monitoring network connectivity state.
 */
interface ConnectivityObserver {

    /**
     * Observes continuous network connectivity changes as a cold reactive flow.
     *
     * @return [Flow] emitting [ConnectionStatus] updates.
     */
    fun observe(): Flow<ConnectionStatus>

    /**
     * Retrieves the instantaneous network connectivity status.
     *
     * @return Current [ConnectionStatus].
     */
    fun getCurrentStatus(): ConnectionStatus
}

/**
 * Implementation of [ConnectivityObserver] utilizing Android [ConnectivityManager].
 *
 * Uses [ConnectivityManager.NetworkCallback] with [NetworkRequest] requiring
 * [NetworkCapabilities.NET_CAPABILITY_INTERNET] to monitor and stream connectivity transitions.
 *
 * @param context Application context used to retrieve [ConnectivityManager].
 */
class NetworkConnectivityObserver(
    private val context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Observes real-time network connectivity changes.
     *
     * Registers a [ConnectivityManager.NetworkCallback] and emits status updates
     * through a [callbackFlow]. Unregisters the callback when flow collection ceases.
     *
     * @return [Flow] emitting distinct [ConnectionStatus] updates.
     */
    override fun observe(): Flow<ConnectionStatus> = callbackFlow {
        // Emit current status immediately upon subscription
        trySend(getCurrentStatus())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(ConnectionStatus.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(ConnectionStatus.Losing)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(ConnectionStatus.Lost)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(ConnectionStatus.Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            try {
                connectivityManager.unregisterNetworkCallback(callback)
            } catch (e: Exception) {
                // Safeguard against callback already unregistered
            }
        }
    }.distinctUntilChanged()

    /**
     * Checks the instantaneous capability flags of the currently active network interface.
     *
     * @return [ConnectionStatus.Available] if internet capability is verified, [ConnectionStatus.Unavailable] otherwise.
     */
    override fun getCurrentStatus(): ConnectionStatus {
        val activeNetwork = connectivityManager.activeNetwork ?: return ConnectionStatus.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return ConnectionStatus.Unavailable

        return if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            ConnectionStatus.Available
        } else {
            ConnectionStatus.Unavailable
        }
    }
}
