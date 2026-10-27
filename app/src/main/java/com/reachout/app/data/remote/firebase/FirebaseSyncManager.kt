package com.reachout.app.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.reachout.app.data.local.entity.AssistanceRequestEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service managing synchronization and real-time operations with Firebase Cloud Firestore.
 */
@Singleton
class FirebaseSyncManager @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val TAG = "FirebaseSyncManager"
        private const val COLLECTION_REQUESTS = "assistance_requests"
        private const val COLLECTION_EMERGENCY_ALERTS = "emergency_alerts"
    }

    /**
     * Uploads or updates an [AssistanceRequestEntity] in Firebase Firestore.
     */
    suspend fun uploadAssistanceRequest(request: AssistanceRequestEntity): Result<Unit> {
        return try {
            val data = mapOf(
                "id" to request.id,
                "assistanceType" to request.assistanceType,
                "description" to request.description,
                "locationDescription" to (request.locationDescription ?: ""),
                "latitude" to request.latitude,
                "longitude" to request.longitude,
                "contactName" to (request.contactName ?: ""),
                "contactNumber" to (request.contactNumber ?: ""),
                "status" to request.status,
                "createdAt" to request.createdAt,
                "updatedAt" to request.updatedAt,
                "syncStatus" to "SYNCED"
            )
            firestore.collection(COLLECTION_REQUESTS)
                .document(request.id)
                .set(data, SetOptions.merge())
                .await()
            Log.d(TAG, "Assistance request ${request.id} successfully synced to Firestore.")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to upload assistance request: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Sends a direct emergency alert to the Firebase Firestore "emergency_alerts" collection.
     */
    suspend fun sendEmergencyAlert(
        title: String,
        description: String,
        contactNumber: String = ""
    ): Result<String> {
        return try {
            val alertData = hashMapOf(
                "title" to title,
                "description" to description,
                "contactNumber" to contactNumber,
                "timestamp" to System.currentTimeMillis(),
                "status" to "ACTIVE"
            )
            val docRef = firestore.collection(COLLECTION_EMERGENCY_ALERTS).add(alertData).await()
            Log.d(TAG, "Emergency alert created with ID: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send emergency alert: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Observes real-time assistance requests from Firebase Cloud Firestore.
     */
    fun observeRemoteRequests(): Flow<List<Map<String, Any>>> = callbackFlow {
        val listenerRegistration = firestore.collection(COLLECTION_REQUESTS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Listen failed: ${error.message}", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { it.data }
                    trySend(items)
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }
}
