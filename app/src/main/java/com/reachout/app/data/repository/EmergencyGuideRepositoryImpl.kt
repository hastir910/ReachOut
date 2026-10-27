package com.reachout.app.data.repository

import com.reachout.app.data.local.dao.EmergencyGuideDao
import com.reachout.app.data.local.entity.EmergencyGuideEntity
import com.reachout.app.domain.repository.EmergencyGuideRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Production implementation of [EmergencyGuideRepository] backed by Room database via [EmergencyGuideDao].
 *
 * Implements offline-first data operations for viewing and maintaining preloaded
 * first-aid instructions, disaster response protocols, and emergency guidance.
 *
 * @property guideDao Room Data Access Object for emergency guide records.
 */
@Singleton
class EmergencyGuideRepositoryImpl @Inject constructor(
    private val guideDao: EmergencyGuideDao
) : EmergencyGuideRepository {

    /**
     * Retrieves all emergency guides available in the local database.
     *
     * @return [Flow] emitting the list of all [EmergencyGuideEntity] items.
     */
    override fun getAllGuides(): Flow<List<EmergencyGuideEntity>> =
        guideDao.getAllGuides()

    /**
     * Retrieves emergency guides filtered by emergency category.
     *
     * @param category The category string to filter by (e.g., FIRST_AID, FIRE, FLOOD).
     * @return [Flow] emitting guides matching the category.
     */
    override fun getGuidesByCategory(category: String): Flow<List<EmergencyGuideEntity>> =
        guideDao.getGuidesByCategory(category)

    /**
     * Retrieves emergency guides available in a specific language.
     *
     * @param language The ISO language code (e.g., "en", "hi").
     * @return [Flow] emitting guides in the specified language.
     */
    override fun getGuidesByLanguage(language: String): Flow<List<EmergencyGuideEntity>> =
        guideDao.getGuidesByLanguage(language)

    /**
     * Retrieves a single emergency guide by its unique identifier.
     *
     * @param id The unique identifier of the guide.
     * @return [Flow] emitting the [EmergencyGuideEntity] or null if not found.
     */
    override fun getGuideById(id: String): Flow<EmergencyGuideEntity?> =
        guideDao.getGuideById(id)

    /**
     * Inserts a single emergency guide into the local database.
     *
     * @param guide The emergency guide entity to insert.
     */
    override suspend fun insertGuide(guide: EmergencyGuideEntity) =
        guideDao.insertGuide(guide)

    /**
     * Inserts a collection of emergency guides into the local database.
     *
     * @param guides The list of emergency guide entities to insert.
     */
    override suspend fun insertGuides(guides: List<EmergencyGuideEntity>) =
        guideDao.insertGuides(guides)
}
