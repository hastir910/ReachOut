package com.reachout.app.domain.repository

import com.reachout.app.data.local.entity.AssistanceRequestEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing assistance requests submitted by community members.
 *
 * Implements offline-first data access pattern returning reactive [Flow]
 * streams for all read operations and suspend functions for database mutations.
 */
interface AssistanceRequestRepository {

    /**
     * Retrieves all assistance requests ordered by creation timestamp descending.
     *
     * @return [Flow] emitting the list of all [AssistanceRequestEntity] items.
     */
    fun getAllRequests(): Flow<List<AssistanceRequestEntity>>

    /**
     * Retrieves a single assistance request by its unique identifier.
     *
     * @param id The unique identifier of the assistance request.
     * @return [Flow] emitting the [AssistanceRequestEntity] or null if not found.
     */
    fun getRequestById(id: String): Flow<AssistanceRequestEntity?>

    /**
     * Retrieves assistance requests filtered by status.
     *
     * @param status The status string to filter by (e.g., DRAFT, PENDING_SYNCHRONISATION).
     * @return [Flow] emitting requests matching the status.
     */
    fun getRequestsByStatus(status: String): Flow<List<AssistanceRequestEntity>>

    /**
     * Retrieves all requests waiting to be synchronized with the remote backend.
     *
     * @return [Flow] emitting requests pending synchronization.
     */
    fun getPendingSyncRequests(): Flow<List<AssistanceRequestEntity>>

    /**
     * Inserts a new assistance request into the local database.
     *
     * @param request The request entity to insert.
     */
    suspend fun insertRequest(request: AssistanceRequestEntity)

    /**
     * Updates an existing assistance request in the local database.
     *
     * @param request The request entity containing updated values.
     */
    suspend fun updateRequest(request: AssistanceRequestEntity)

    /**
     * Updates the status and last modified timestamp of an assistance request.
     *
     * @param id The unique identifier of the assistance request.
     * @param status The new request status string.
     * @param updatedAt Epoch timestamp in milliseconds of the update.
     */
    suspend fun updateRequestStatus(id: String, status: String, updatedAt: Long)

    /**
     * Deletes an assistance request from the local database.
     *
     * @param request The request entity to remove.
     */
    suspend fun deleteRequest(request: AssistanceRequestEntity)
}
