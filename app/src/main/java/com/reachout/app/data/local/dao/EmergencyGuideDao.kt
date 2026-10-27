package com.reachout.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reachout.app.data.local.entity.EmergencyGuideEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for offline emergency guides and first-aid protocols.
 *
 * Provides reactive access to preloaded crisis response guides for offline viewing.
 */
@Dao
interface EmergencyGuideDao {

    /**
     * Retrieves all emergency guides sorted by sort order and title.
     */
    @Query("SELECT * FROM emergency_guides ORDER BY sortOrder ASC, title ASC")
    fun getAllGuides(): Flow<List<EmergencyGuideEntity>>

    /**
     * Retrieves emergency guides by category (e.g. MEDICAL, POLICE, FIRE, FIRST_AID).
     *
     * @param category Guide category string.
     */
    @Query("SELECT * FROM emergency_guides WHERE category = :category ORDER BY sortOrder ASC, title ASC")
    fun getGuidesByCategory(category: String): Flow<List<EmergencyGuideEntity>>

    /**
     * Retrieves emergency guides matching a specific language code.
     *
     * @param language ISO language code (e.g. "en", "hi").
     */
    @Query("SELECT * FROM emergency_guides WHERE language = :language ORDER BY sortOrder ASC, title ASC")
    fun getGuidesByLanguage(language: String): Flow<List<EmergencyGuideEntity>>

    /**
     * Observes a specific emergency guide by its unique identifier.
     *
     * @param id Unique guide UUID.
     */
    @Query("SELECT * FROM emergency_guides WHERE id = :id LIMIT 1")
    fun getGuideById(id: String): Flow<EmergencyGuideEntity?>

    /**
     * Inserts or replaces an emergency guide.
     *
     * @param guide The emergency guide entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuide(guide: EmergencyGuideEntity)

    /**
     * Inserts or replaces a list of emergency guides in bulk.
     *
     * @param guides The list of emergency guide entities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuides(guides: List<EmergencyGuideEntity>)

    /**
     * Updates an existing emergency guide.
     *
     * @param guide The emergency guide entity with updated content.
     */
    @Update
    suspend fun updateGuide(guide: EmergencyGuideEntity)

    /**
     * Deletes all emergency guides from local storage.
     */
    @Query("DELETE FROM emergency_guides")
    suspend fun deleteAllGuides()
}
