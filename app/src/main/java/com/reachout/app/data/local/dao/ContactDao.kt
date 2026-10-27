package com.reachout.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reachout.app.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local community contacts.
 *
 * Exposes reactive [Flow] streams for real-time UI updates and offline directory searches,
 * alongside suspend functions for offline mutations and synchronization tasks.
 */
@Dao
interface ContactDao {

    /**
     * Retrieves all contacts ordered alphabetically by name.
     */
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    /**
     * Retrieves contacts matching the specified service category.
     *
     * @param category Service category string (e.g. HOSPITAL, POLICE, AMBULANCE).
     */
    @Query("SELECT * FROM contacts WHERE serviceCategory = :category ORDER BY name ASC")
    fun getContactsByCategory(category: String): Flow<List<ContactEntity>>

    /**
     * Retrieves contacts bookmarked/saved by the user for fast offline access.
     */
    @Query("SELECT * FROM contacts WHERE isSaved = 1 ORDER BY name ASC")
    fun getSavedContacts(): Flow<List<ContactEntity>>

    /**
     * Searches contacts across name, area, district, and landmark using substring matching.
     *
     * @param query Search query string.
     */
    @Query(
        """
        SELECT * FROM contacts 
        WHERE name LIKE '%' || :query || '%' 
           OR area LIKE '%' || :query || '%' 
           OR district LIKE '%' || :query || '%' 
           OR landmark LIKE '%' || :query || '%' 
        ORDER BY name ASC
        """
    )
    fun searchContacts(query: String): Flow<List<ContactEntity>>

    /**
     * Observes a single contact by its unique identifier.
     *
     * @param id Unique contact identifier.
     */
    @Query("SELECT * FROM contacts WHERE id = :id LIMIT 1")
    fun getContactById(id: String): Flow<ContactEntity?>

    /**
     * Retrieves contacts filtered by verification status (VERIFIED, UNVERIFIED, EXPIRED, SUSPENDED).
     *
     * @param status Verification status string.
     */
    @Query("SELECT * FROM contacts WHERE verificationStatus = :status ORDER BY name ASC")
    fun getContactsByVerificationStatus(status: String): Flow<List<ContactEntity>>

    /**
     * Inserts or replaces a contact record.
     *
     * @param contact The contact entity to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    /**
     * Inserts or replaces multiple contact records in bulk.
     *
     * @param contacts The list of contact entities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    /**
     * Updates an existing contact record.
     *
     * @param contact The contact entity to update.
     */
    @Update
    suspend fun updateContact(contact: ContactEntity)

    /**
     * Deletes a contact record from the database.
     *
     * @param contact The contact entity to delete.
     */
    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    /**
     * Updates the saved/bookmarked flag for a specific contact.
     *
     * @param id Unique contact identifier.
     * @param isSaved True to bookmark, false to remove from saved list.
     */
    @Query("UPDATE contacts SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: String, isSaved: Boolean)

    /**
     * Observes the total number of contacts stored locally.
     */
    @Query("SELECT COUNT(*) FROM contacts")
    fun getContactCount(): Flow<Int>

    /**
     * Retrieves all contacts flagged as awaiting network synchronization.
     */
    @Query("SELECT * FROM contacts WHERE syncStatus = 'PENDING_SYNC' ORDER BY updatedAt DESC")
    fun getPendingSyncContacts(): Flow<List<ContactEntity>>
}
