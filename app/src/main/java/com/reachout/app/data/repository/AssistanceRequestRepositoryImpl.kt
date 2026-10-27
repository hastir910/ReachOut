package com.reachout.app.data.repository

import com.reachout.app.data.local.dao.AssistanceRequestDao
import com.reachout.app.data.local.entity.AssistanceRequestEntity
import com.reachout.app.domain.repository.AssistanceRequestRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Production implementation of [AssistanceRequestRepository] backed by Room database via [AssistanceRequestDao].
 *
 * Implements offline-first data operations for creating, updating, monitoring,
 * and deleting community assistance requests.
 *
 * @property requestDao Room Data Access Object for assistance requests.
 */
@Singleton
class AssistanceRequestRepositoryImpl @Inject constructor(
    private val requestDao: AssistanceRequestDao
) : AssistanceRequestRepository {

    /**
     * Retrieves all assistance requests ordered by creation timestamp descending.
     *
     * @return [Flow] emitting the list of all [AssistanceRequestEntity] items.
     */
    override fun getAllRequests(): Flow<List<AssistanceRequestEntity>> =
        requestDao.getAllRequests()

    /**
     * Retrieves a single assistance request by its unique identifier.
     *
     * @param id The unique identifier of the assistance request.
     * @return [Flow] emitting the [AssistanceRequestEntity] or null if not found.
     */
    override fun getRequestById(id: String): Flow<AssistanceRequestEntity?> =
        requestDao.getRequestById(id)

    /**
     * Retrieves assistance requests filtered by status.
     *
     * @param status The status string to filter by (e.g., DRAFT, PENDING_SYNCHRONISATION).
     * @return [Flow] emitting requests matching the status.
     */
    override fun getRequestsByStatus(status: String): Flow<List<AssistanceRequestEntity>> =
        requestDao.getRequestsByStatus(status)

    /**
     * Retrieves all requests waiting to be synchronized with the remote backend.
     *
     * @return [Flow] emitting requests pending synchronization.
     */
    override fun getPendingSyncRequests(): Flow<List<AssistanceRequestEntity>> =
        requestDao.getPendingSyncRequests()

    /**
     * Inserts a new assistance request into the local database.
     *
     * @param request The request entity to insert.
     */
    override suspend fun insertRequest(request: AssistanceRequestEntity) =
        requestDao.insertRequest(request)

    /**
     * Updates an existing assistance request in the local database.
     *
     * @param request The request entity containing updated values.
     */
    override suspend fun updateRequest(request: AssistanceRequestEntity) =
        requestDao.updateRequest(request)

    /**
     * Updates the status and last modified timestamp of an assistance request.
     *
     * @param id The unique identifier of the assistance request.
     * @param status The new request status string.
     * @param updatedAt Epoch timestamp in milliseconds of the update.
     */
    override suspend fun updateRequestStatus(id: String, status: String, updatedAt: Long) =
        requestDao.updateRequestStatus(id, status, updatedAt)

    /**
     * Deletes an assistance request from the local database.
     *
     * @param request The request entity to remove.
     */
    override suspend fun deleteRequest(request: AssistanceRequestEntity) =
        requestDao.deleteRequest(request)
}
