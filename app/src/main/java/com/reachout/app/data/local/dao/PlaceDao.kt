package com.reachout.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reachout.app.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Safe Places facilities and community points of interest.
 *
 * Provides reactive read streams for map filtering and offline directory navigation,
 * and suspend write routines for syncing and offline bookmarking.
 */
@Dao
interface PlaceDao {

    /**
     * Retrieves all safe places ordered alphabetically by name.
     */
    @Query("SELECT * FROM places ORDER BY name ASC")
    fun getAllPlaces(): Flow<List<PlaceEntity>>

    /**
     * Retrieves safe places filtered by their facility type.
     *
     * @param placeType Type filter (e.g. HOSPITAL, CLINIC, SHELTER, POLICE_STATION).
     */
    @Query("SELECT * FROM places WHERE placeType = :placeType ORDER BY name ASC")
    fun getPlacesByType(placeType: String): Flow<List<PlaceEntity>>

    /**
     * Retrieves safe places bookmarked/pinned by the user for offline fast access.
     */
    @Query("SELECT * FROM places WHERE isSaved = 1 ORDER BY name ASC")
    fun getSavedPlaces(): Flow<List<PlaceEntity>>

    /**
     * Searches safe places by matching name, area, district, landmark, or address.
     *
     * @param query Search query text.
     */
    @Query(
        """
        SELECT * FROM places 
        WHERE name LIKE '%' || :query || '%' 
           OR area LIKE '%' || :query || '%' 
           OR district LIKE '%' || :query || '%' 
           OR landmark LIKE '%' || :query || '%' 
           OR address LIKE '%' || :query || '%' 
        ORDER BY name ASC
        """
    )
    fun searchPlaces(query: String): Flow<List<PlaceEntity>>

    /**
     * Observes a specific place by its unique identifier.
     *
     * @param id Unique place identifier.
     */
    @Query("SELECT * FROM places WHERE id = :id LIMIT 1")
    fun getPlaceById(id: String): Flow<PlaceEntity?>

    /**
     * Inserts or replaces a safe place record in the database.
     *
     * @param place The place entity to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceEntity)

    /**
     * Inserts or replaces a collection of safe places in bulk.
     *
     * @param places The list of place entities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    /**
     * Updates an existing safe place record.
     *
     * @param place The place entity with updated properties.
     */
    @Update
    suspend fun updatePlace(place: PlaceEntity)

    /**
     * Deletes a safe place record from the database.
     *
     * @param place The place entity to remove.
     */
    @Delete
    suspend fun deletePlace(place: PlaceEntity)

    /**
     * Updates the saved/bookmarked flag for a specific place.
     *
     * @param id Unique place identifier.
     * @param isSaved True if saved/pinned, false otherwise.
     */
    @Query("UPDATE places SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: String, isSaved: Boolean)

    /**
     * Observes the total number of safe places stored locally.
     */
    @Query("SELECT COUNT(*) FROM places")
    fun getPlaceCount(): Flow<Int>
}
