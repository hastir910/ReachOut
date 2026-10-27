package com.reachout.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reachout.app.data.local.entity.PendingSyncEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for managing pending offline network synchronization operations.
 *
 * Implements the offline outbox pattern to queue, process, and clear mutations once
 * network connectivity is restored.
 */
@Dao
interface PendingSyncDao {

    /**
     * Retrieves all pending synchronisation entries ordered by creation timestamp ascending.
     */
    @Query("SELECT * FROM pending_sync ORDER BY createdAt ASC")
    fun getAllPending(): Flow<List<PendingSyncEntity>>

    /**
     * Retrieves synchronisation entries filtered by status (PENDING, IN_PROGRESS, FAILED, COMPLETED).
     *
     * @param status Status string to filter on.
     */
    @Query("SELECT * FROM pending_sync WHERE status = :status ORDER BY createdAt ASC")
    fun getPendingByStatus(status: String): Flow<List<PendingSyncEntity>>

    /**
     * Retrieves synchronisation entries filtered by target entity type (CONTACT, PLACE, REQUEST, REPORT).
     *
     * @param entityType Entity type string.
     */
    @Query("SELECT * FROM pending_sync WHERE entityType = :entityType ORDER BY createdAt ASC")
    fun getPendingByEntityType(entityType: String): Flow<List<PendingSyncEntity>>

    /**
     * Inserts or replaces a pending sync entry.
     *
     * @param sync The pending sync entity to queue.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingSync(sync: PendingSyncEntity)

    /**
     * Updates an existing pending sync entry (e.g. status transition, retry count increment).
     *
     * @param sync The pending sync entity with updated state.
     */
    @Update
    suspend fun updatePendingSync(sync: PendingSyncEntity)

    /**
     * Removes a specific pending sync entry from the queue.
     *
     * @param sync The pending sync entity to remove.
     */
    @Delete
    suspend fun deletePendingSync(sync: PendingSyncEntity)

    /**
     * Purges all completed sync tasks from the outbox table.
     */
    @Query("DELETE FROM pending_sync WHERE status = 'COMPLETED'")
    suspend fun deleteCompletedSyncs()

    /**
     * Observes the count of pending operations currently waiting for synchronisation.
     */
    @Query("SELECT COUNT(*) FROM pending_sync WHERE status = 'PENDING'")
    fun getPendingCount(): Flow<Int>

    /**
     * Retrieves a pending sync entry by its associated entity ID, if one exists.
     *
     * @param entityId Target entity identifier.
     * @return The pending sync item or null if none is currently queued.
     */
    @Query("SELECT * FROM pending_sync WHERE entityId = :entityId LIMIT 1")
    suspend fun getByEntityId(entityId: String): PendingSyncEntity?
}
