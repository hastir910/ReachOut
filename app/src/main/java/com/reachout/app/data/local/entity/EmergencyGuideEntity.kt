package com.reachout.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing an offline emergency guide or first-aid protocol.
 *
 * Preloaded into local SQLite storage to guarantee immediate, lifecycle-critical guidance
 * in life-threatening scenarios without requiring any network connectivity.
 *
 * @property id Unique UUID string identifying this emergency guide.
 * @property title Heading of the guide.
 * @property category Category (MEDICAL, POLICE, FIRE, NATURAL_DISASTER, GENERAL, FIRST_AID).
 * @property content Comprehensive instructions and protocols stored in markdown/plain text.
 * @property language ISO 639-1 language code (defaults to "en").
 * @property region Optional region identifier if protocol has jurisdiction-specific rules.
 * @property emergencyNumber Direct dial emergency telephone number associated with this guide.
 * @property lastUpdatedDate Epoch millisecond timestamp of last guide revision.
 * @property isSampleData Flag indicating demo/seeded data.
 * @property sortOrder Display priority ordering.
 */
@Entity(tableName = "emergency_guides")
data class EmergencyGuideEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val content: String,
    val language: String = "en",
    val region: String? = null,
    val emergencyNumber: String? = null,
    val lastUpdatedDate: Long,
    val isSampleData: Boolean = false,
    val sortOrder: Int = 0
)
