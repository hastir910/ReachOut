package com.reachout.app.domain.repository

import com.reachout.app.data.local.entity.EmergencyGuideEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing offline emergency guides and action checklists.
 *
 * Implements offline-first data access pattern returning reactive [Flow]
 * streams for all read operations and suspend functions for database mutations.
 */
interface EmergencyGuideRepository {

    /**
     * Retrieves all emergency guides available in the local database.
     *
     * @return [Flow] emitting the list of all [EmergencyGuideEntity] items.
     */
    fun getAllGuides(): Flow<List<EmergencyGuideEntity>>

    /**
     * Retrieves emergency guides filtered by emergency category.
     *
     * @param category The category string to filter by (e.g., FIRST_AID, FIRE, FLOOD).
     * @return [Flow] emitting guides matching the category.
     */
    fun getGuidesByCategory(category: String): Flow<List<EmergencyGuideEntity>>

    /**
     * Retrieves emergency guides available in a specific language.
     *
     * @param language The ISO language code (e.g., "en", "sw").
     * @return [Flow] emitting guides in the specified language.
     */
    fun getGuidesByLanguage(language: String): Flow<List<EmergencyGuideEntity>>

    /**
     * Retrieves a single emergency guide by its unique identifier.
     *
     * @param id The unique identifier of the guide.
     * @return [Flow] emitting the [EmergencyGuideEntity] or null if not found.
     */
    fun getGuideById(id: String): Flow<EmergencyGuideEntity?>

    /**
     * Inserts a single emergency guide into the local database.
     *
     * @param guide The emergency guide entity to insert.
     */
    suspend fun insertGuide(guide: EmergencyGuideEntity)

    /**
     * Inserts a collection of emergency guides into the local database.
     *
     * @param guides The list of emergency guide entities to insert.
     */
    suspend fun insertGuides(guides: List<EmergencyGuideEntity>)
}
