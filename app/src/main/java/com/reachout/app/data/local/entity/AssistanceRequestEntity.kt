package com.reachout.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing an offline community assistance request.
 *
 * Designed to queue requests locally when no internet connection is present and track
 * lifecycle states through synchronization, helper matching, and resolution.
 *
 * @property id Unique UUID string identifying this assistance request.
 * @property assistanceType Type of assistance (MEDICAL, TRANSPORT, AGRICULTURAL, VEHICLE_BREAKDOWN, COMMUNITY_SUPPORT, EMERGENCY, OTHER).
 * @property description Detailed description of help required.
 * @property locationDescription User-entered manual location description or landmark.
 * @property latitude Optional GPS latitude coordinate if shared.
 * @property longitude Optional GPS longitude coordinate if shared.
 * @property contactName Requester contact name.
 * @property contactNumber Requester callback telephone number.
 * @property locationSharingConsent Flag confirming whether user gave explicit consent to share GPS coordinates.
 * @property status Workflow state (DRAFT, SAVED_OFFLINE_NOT_SENT, PENDING_SYNCHRONISATION, SENT_AWAITING_RESPONSE, ACCEPTED_BY_HELPER, COMPLETED, FAILED_TO_SEND).
 * @property createdAt Epoch millisecond timestamp when request was drafted/created.
 * @property updatedAt Epoch millisecond timestamp of last update.
 * @property sentAt Epoch millisecond timestamp when successfully sent to the server.
 * @property acceptedAt Epoch millisecond timestamp when accepted by a community helper.
 * @property completedAt Epoch millisecond timestamp when marked completed.
 * @property retryCount Number of automated background sync attempts.
 * @property syncStatus Network synchronisation status.
 * @property failureReason Diagnostic error message if sync or transmission failed.
 */
@Entity(tableName = "assistance_requests")
data class AssistanceRequestEntity(
    @PrimaryKey
    val id: String,
    val assistanceType: String,
    val description: String,
    val locationDescription: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val contactName: String? = null,
    val contactNumber: String? = null,
    val locationSharingConsent: Boolean = false,
    val status: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val sentAt: Long? = null,
    val acceptedAt: Long? = null,
    val completedAt: Long? = null,
    val retryCount: Int = 0,
    val syncStatus: String,
    val failureReason: String? = null
)
