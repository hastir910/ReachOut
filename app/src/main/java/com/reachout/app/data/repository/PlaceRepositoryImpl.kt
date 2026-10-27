package com.reachout.app.data.repository

import com.reachout.app.data.local.dao.PlaceDao
import com.reachout.app.data.local.entity.PlaceEntity
import com.reachout.app.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Production implementation of [PlaceRepository] backed by Room database via [PlaceDao].
 *
 * Implements offline-first reactive reads via Kotlin [Flow] and asynchronous suspend
 * operations for local place writes, updates, deletions, and bookmarking.
 *
 * @property placeDao Room Data Access Object for safe place records.
 */
@Singleton
class PlaceRepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao
) : PlaceRepository {

    /**
     * Retrieves all places stored in the local database.
     *
     * @return [Flow] emitting the list of all [PlaceEntity] items.
     */
    override fun getAllPlaces(): Flow<List<PlaceEntity>> =
        placeDao.getAllPlaces()

    /**
     * Retrieves places belonging to a specific place type.
     *
     * @param placeType The place type category string (e.g., HOSPITAL, POLICE_STATION).
     * @return [Flow] emitting the filtered list of [PlaceEntity] items.
     */
    override fun getPlacesByType(placeType: String): Flow<List<PlaceEntity>> =
        placeDao.getPlacesByType(placeType)

    /**
     * Retrieves all places bookmarked or saved by the user for quick access.
     *
     * @return [Flow] emitting the list of saved [PlaceEntity] items.
     */
    override fun getSavedPlaces(): Flow<List<PlaceEntity>> =
        placeDao.getSavedPlaces()

    /**
     * Searches places by name, address, or description.
     *
     * @param query Search query text.
     * @return [Flow] emitting matching [PlaceEntity] items.
     */
    override fun searchPlaces(query: String): Flow<List<PlaceEntity>> =
        placeDao.searchPlaces(query)

    /**
     * Retrieves a place by its unique identifier.
     *
     * @param id The unique identifier of the place.
     * @return [Flow] emitting the [PlaceEntity] or null if not found.
     */
    override fun getPlaceById(id: String): Flow<PlaceEntity?> =
        placeDao.getPlaceById(id)

    /**
     * Inserts a new place into the local database.
     *
     * @param place The place entity to insert.
     */
    override suspend fun insertPlace(place: PlaceEntity) =
        placeDao.insertPlace(place)

    /**
     * Updates an existing place in the local database.
     *
     * @param place The place entity containing updated values.
     */
    override suspend fun updatePlace(place: PlaceEntity) =
        placeDao.updatePlace(place)

    /**
     * Deletes a place from the local database.
     *
     * @param place The place entity to remove.
     */
    override suspend fun deletePlace(place: PlaceEntity) =
        placeDao.deletePlace(place)

    /**
     * Updates the saved/bookmarked status of a specific place.
     *
     * @param id The unique identifier of the place.
     * @param isSaved True to bookmark the place, false to remove the bookmark.
     */
    override suspend fun updateSavedStatus(id: String, isSaved: Boolean) =
        placeDao.updateSavedStatus(id, isSaved)
}
