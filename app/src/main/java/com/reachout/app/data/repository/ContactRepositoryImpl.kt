package com.reachout.app.data.repository

import com.reachout.app.data.local.dao.ContactDao
import com.reachout.app.data.local.entity.ContactEntity
import com.reachout.app.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Production implementation of [ContactRepository] backed by Room database via [ContactDao].
 *
 * Implements offline-first reactive reads via Kotlin [Flow] and asynchronous suspend
 * operations for local writes and bookmark toggling.
 *
 * @property contactDao Room Data Access Object for contact records.
 */
@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) : ContactRepository {

    /**
     * Retrieves all contacts stored in the local database.
     *
     * @return [Flow] emitting the list of all [ContactEntity] items.
     */
    override fun getAllContacts(): Flow<List<ContactEntity>> =
        contactDao.getAllContacts()

    /**
     * Retrieves contacts matching a specific service category.
     *
     * @param category The category filter string (e.g., HOSPITAL, POLICE).
     * @return [Flow] emitting the filtered list of [ContactEntity] items.
     */
    override fun getContactsByCategory(category: String): Flow<List<ContactEntity>> =
        contactDao.getContactsByCategory(category)

    /**
     * Retrieves all contacts bookmarked or saved by the user for quick access.
     *
     * @return [Flow] emitting the list of saved [ContactEntity] items.
     */
    override fun getSavedContacts(): Flow<List<ContactEntity>> =
        contactDao.getSavedContacts()

    /**
     * Searches contacts by name, service category, or description.
     *
     * @param query Search query text.
     * @return [Flow] emitting matching [ContactEntity] items.
     */
    override fun searchContacts(query: String): Flow<List<ContactEntity>> =
        contactDao.searchContacts(query)

    /**
     * Retrieves a single contact by its unique identifier.
     *
     * @param id The unique identifier of the contact.
     * @return [Flow] emitting the [ContactEntity] or null if not found.
     */
    override fun getContactById(id: String): Flow<ContactEntity?> =
        contactDao.getContactById(id)

    /**
     * Inserts a new contact or replaces an existing one in the local database.
     *
     * @param contact The contact entity to insert.
     */
    override suspend fun insertContact(contact: ContactEntity) =
        contactDao.insertContact(contact)

    /**
     * Updates an existing contact in the local database.
     *
     * @param contact The contact entity containing updated values.
     */
    override suspend fun updateContact(contact: ContactEntity) =
        contactDao.updateContact(contact)

    /**
     * Deletes a contact from the local database.
     *
     * @param contact The contact entity to remove.
     */
    override suspend fun deleteContact(contact: ContactEntity) =
        contactDao.deleteContact(contact)

    /**
     * Updates the saved/bookmarked status of a specific contact.
     *
     * @param id The unique identifier of the contact.
     * @param isSaved True to bookmark the contact, false to remove the bookmark.
     */
    override suspend fun updateSavedStatus(id: String, isSaved: Boolean) =
        contactDao.updateSavedStatus(id, isSaved)

    /**
     * Retrieves the total count of contacts available in the local database.
     *
     * @return [Flow] emitting the current count of contacts.
     */
    override fun getContactCount(): Flow<Int> =
        contactDao.getContactCount()
}
