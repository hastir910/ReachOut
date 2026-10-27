package com.reachout.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing a Safe Place facility or community point of interest.
 *
 * Persisted locally so community members can locate emergency shelters, clinics, police stations,
 * and aid distribution points even in complete network blackouts.
 *
 * @property id Unique UUID string identifying this safe place.
 * @property name Official or common name of the facility or help point.
 * @property placeType Facility classification (HOSPITAL, CLINIC, PHARMACY, POLICE_STATION, FIRE_STATION, EMERGENCY_FACILITY, COMMUNITY_CENTRE, NGO_CENTRE, SHELTER, COMMUNITY_ASSISTANCE_POINT, OTHER).
 * @property address Physical street address.
 * @property landmark Key landmark reference for local navigation.
 * @property area Neighbourhood or locality area.
 * @property district Administrative district.
 * @property state State or provincial jurisdiction.
 * @property contactNumber Main telephone or helpline number.
 * @property servicesAvailable Comma-separated or structured amenities available at this location.
 * @property operatingHours Working hours string (e.g., "24/7", "06:00 - 22:00").
 * @property latitude WGS84 latitude coordinate.
 * @property longitude WGS84 longitude coordinate.
 * @property verificationStatus Reliability rating (VERIFIED_FACILITY, VERIFIED_COMMUNITY_HELP_POINT, UNVERIFIED_LISTING, NOT_RECENTLY_CONFIRMED).
 * @property verifiedBy Authority or organisation that verified this location.
 * @property verificationDate Epoch millisecond timestamp of verification.
 * @property lastUpdatedDate Epoch millisecond timestamp of last update.
 * @property isSaved Flag indicating whether user pinned/saved this place for offline quick access.
 * @property isSampleData Flag indicating preloaded demo/seed data.
 * @property syncStatus Synchronization status with remote server.
 */
@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val placeType: String,
    val address: String? = null,
    val landmark: String? = null,
    val area: String? = null,
    val district: String? = null,
    val state: String? = null,
    val contactNumber: String? = null,
    val servicesAvailable: String? = null,
    val operatingHours: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val verificationStatus: String,
    val verifiedBy: String? = null,
    val verificationDate: Long? = null,
    val lastUpdatedDate: Long,
    val isSaved: Boolean = false,
    val isSampleData: Boolean = false,
    val syncStatus: String
)
