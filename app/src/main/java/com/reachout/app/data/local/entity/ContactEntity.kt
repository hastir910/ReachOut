package com.reachout.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing a local community contact stored offline.
 *
 * Provides quick offline access to critical community contacts including hospitals,
 * police, fire stations, ambulances, agricultural officers, volunteers, and NGOs.
 *
 * @property id Unique UUID string identifying this contact.
 * @property name Human-readable name of the individual, organisation, or facility.
 * @property serviceCategory High-level category (e.g. HOSPITAL, POLICE, AMBULANCE, etc.).
 * @property contactNumber Primary dialable phone number.
 * @property alternateNumber Secondary dialable phone number, if available.
 * @property address Physical street address description.
 * @property landmark Key landmark used to identify the location in offline situations.
 * @property area Neighbourhood or geographical area name.
 * @property locality Specific locality, town, or village name.
 * @property district Administrative district.
 * @property state State or provincial jurisdiction.
 * @property operatingHours Human-readable operating schedule (e.g. "24/7", "08:00 - 18:00").
 * @property servicesProvided Comma-delimited or structured list of services offered.
 * @property verificationStatus Verification tier (VERIFIED, UNVERIFIED, EXPIRED, SUSPENDED).
 * @property verifiedBy Identifier or name of the official or agency verifying the listing.
 * @property verificationDate Epoch millisecond timestamp when verified.
 * @property lastUpdatedDate Epoch millisecond timestamp of last verified update.
 * @property source Attribution source of the contact information.
 * @property availabilityInfo Dynamic or textual availability details.
 * @property latitude WGS84 latitude coordinate for offline mapping.
 * @property longitude WGS84 longitude coordinate for offline mapping.
 * @property isSaved Flag indicating whether user bookmarked this contact for quick offline access.
 * @property isSampleData Flag indicating preloaded demo/seed data.
 * @property createdAt Epoch millisecond timestamp when record was created locally.
 * @property updatedAt Epoch millisecond timestamp when record was last updated locally.
 * @property syncStatus Network sync state (SYNCED, PENDING_SYNC, LOCAL_ONLY).
 */
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val serviceCategory: String,
    val contactNumber: String,
    val alternateNumber: String? = null,
    val address: String? = null,
    val landmark: String? = null,
    val area: String? = null,
    val locality: String? = null,
    val district: String? = null,
    val state: String? = null,
    val operatingHours: String? = null,
    val servicesProvided: String? = null,
    val verificationStatus: String,
    val verifiedBy: String? = null,
    val verificationDate: Long? = null,
    val lastUpdatedDate: Long = System.currentTimeMillis(),
    val source: String? = null,
    val availabilityInfo: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isSaved: Boolean = false,
    val isSampleData: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String
)
