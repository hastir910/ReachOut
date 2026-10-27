package com.reachout.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reachout.app.data.local.entity.AssistanceRequestEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for offline community assistance requests.
 *
 * Supports creating and persisting assistance requests while offline, monitoring
 * their synchronization and fulfillment status, and querying request history.
 */
@Dao
interface AssistanceRequestDao {

    /**
     * Retrieves all assistance requests sorted by creation date descending.
     */
    @Query("SELECT * FROM assistance_requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<AssistanceRequestEntity>>

    /**
     * Observes an individual assistance request by its unique identifier.
     *
     * @param id Unique request UUID.
     */
    @Query("SELECT * FROM assistance_requests WHERE id = :id LIMIT 1")
    fun getRequestById(id: String): Flow<AssistanceRequestEntity?>

    /**
     * Retrieves assistance requests filtered by their workflow status.
     *
     * @param status Status filter (e.g. DRAFT, SAVED_OFFLINE_NOT_SENT, PENDING_SYNCHRONISATION, etc.).
     */
    @Query("SELECT * FROM assistance_requests WHERE status = :status ORDER BY createdAt DESC")
    fun getRequestsByStatus(status: String): Flow<List<AssistanceRequestEntity>>

    /**
     * Retrieves requests that are queued or pending network synchronisation.
     */
    @Query(
        """
        SELECT * FROM assistance_requests 
        WHERE syncStatus = 'PENDING_SYNC' 
           OR syncStatus = 'PENDING_SYNCHRONISATION' 
           OR status = 'PENDING_SYNCHRONISATION' 
        ORDER BY createdAt ASC
        """
    )
    fun getPendingSyncRequests(): Flow<List<AssistanceRequestEntity>>

    /**
     * Inserts or replaces an assistance request.
     *
     * @param request The assistance request to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: AssistanceRequestEntity)

    /**
     * Updates an existing assistance request record.
     *
     * @param request The assistance request entity with updated properties.
     */
    @Update
    suspend fun updateRequest(request: AssistanceRequestEntity)

    /**
     * Updates the workflow status and modification timestamp of an assistance request.
     *
     * @param id Unique request UUID.
     * @param status New status string.
     * @param updatedAt Epoch timestamp of the status update.
     */
    @Query("UPDATE assistance_requests SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateRequestStatus(id: String, status: String, updatedAt: Long)

    /**
     * Deletes an assistance request from local storage.
     *
     * @param request The assistance request entity to delete.
     */
    @Delete
    suspend fun deleteRequest(request: AssistanceRequestEntity)

    /**
     * Observes the total number of assistance requests stored locally.
     */
    @Query("SELECT COUNT(*) FROM assistance_requests")
    fun getRequestCount(): Flow<Int>
}
