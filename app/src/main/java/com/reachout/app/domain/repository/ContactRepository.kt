package com.reachout.app.domain.repository

import com.reachout.app.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing community and emergency contacts.
 *
 * Implements offline-first data access pattern returning reactive [Flow]
 * streams for all read operations and suspend functions for database mutations.
 */
interface ContactRepository {

    /**
     * Retrieves all contacts stored in the local database.
     *
     * @return [Flow] emitting the list of all [ContactEntity] items.
     */
    fun getAllContacts(): Flow<List<ContactEntity>>

    /**
     * Retrieves contacts matching a specific service category.
     *
     * @param category The category filter string (e.g., HOSPITAL, POLICE).
     * @return [Flow] emitting the filtered list of [ContactEntity] items.
     */
    fun getContactsByCategory(category: String): Flow<List<ContactEntity>>

    /**
     * Retrieves all contacts bookmarked or saved by the user for quick access.
     *
     * @return [Flow] emitting the list of saved [ContactEntity] items.
     */
    fun getSavedContacts(): Flow<List<ContactEntity>>

    /**
     * Searches contacts by name, service category, or description.
     *
     * @param query Search query text.
     * @return [Flow] emitting matching [ContactEntity] items.
     */
    fun searchContacts(query: String): Flow<List<ContactEntity>>

    /**
     * Retrieves a single contact by its unique identifier.
     *
     * @param id The unique identifier of the contact.
     * @return [Flow] emitting the [ContactEntity] or null if not found.
     */
    fun getContactById(id: String): Flow<ContactEntity?>

    /**
     * Inserts a new contact or replaces an existing one in the local database.
     *
     * @param contact The contact entity to insert.
     */
    suspend fun insertContact(contact: ContactEntity)

    /**
     * Updates an existing contact in the local database.
     *
     * @param contact The contact entity containing updated values.
     */
    suspend fun updateContact(contact: ContactEntity)

    /**
     * Deletes a contact from the local database.
     *
     * @param contact The contact entity to remove.
     */
    suspend fun deleteContact(contact: ContactEntity)

    /**
     * Updates the saved/bookmarked status of a specific contact.
     *
     * @param id The unique identifier of the contact.
     * @param isSaved True to bookmark the contact, false to remove the bookmark.
     */
    suspend fun updateSavedStatus(id: String, isSaved: Boolean)

    /**
     * Retrieves the total count of contacts available in the local database.
     *
     * @return [Flow] emitting the current count of contacts.
     */
    fun getContactCount(): Flow<Int>
}
