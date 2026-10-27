package com.reachout.app.domain.repository

import com.reachout.app.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing physical community places, clinics, and emergency facilities.
 *
 * Implements offline-first data access pattern returning reactive [Flow]
 * streams for all read operations and suspend functions for database mutations.
 */
interface PlaceRepository {

    /**
     * Retrieves all places stored in the local database.
     *
     * @return [Flow] emitting the list of all [PlaceEntity] items.
     */
    fun getAllPlaces(): Flow<List<PlaceEntity>>

    /**
     * Retrieves places belonging to a specific place type.
     *
     * @param placeType The place type category string (e.g., HOSPITAL, POLICE_STATION).
     * @return [Flow] emitting the filtered list of [PlaceEntity] items.
     */
    fun getPlacesByType(placeType: String): Flow<List<PlaceEntity>>

    /**
     * Retrieves all places bookmarked or saved by the user for quick access.
     *
     * @return [Flow] emitting the list of saved [PlaceEntity] items.
     */
    fun getSavedPlaces(): Flow<List<PlaceEntity>>

    /**
     * Searches places by name, address, or description.
     *
     * @param query Search query text.
     * @return [Flow] emitting matching [PlaceEntity] items.
     */
    fun searchPlaces(query: String): Flow<List<PlaceEntity>>

    /**
     * Retrieves a place by its unique identifier.
     *
     * @param id The unique identifier of the place.
     * @return [Flow] emitting the [PlaceEntity] or null if not found.
     */
    fun getPlaceById(id: String): Flow<PlaceEntity?>

    /**
     * Inserts a new place into the local database.
     *
     * @param place The place entity to insert.
     */
    suspend fun insertPlace(place: PlaceEntity)

    /**
     * Updates an existing place in the local database.
     *
     * @param place The place entity containing updated values.
     */
    suspend fun updatePlace(place: PlaceEntity)

    /**
     * Deletes a place from the local database.
     *
     * @param place The place entity to remove.
     */
    suspend fun deletePlace(place: PlaceEntity)

    /**
     * Updates the saved/bookmarked status of a specific place.
     *
     * @param id The unique identifier of the place.
     * @param isSaved True to bookmark the place, false to remove the bookmark.
     */
    suspend fun updateSavedStatus(id: String, isSaved: Boolean)
}
