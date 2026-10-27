package com.reachout.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity tracking pending offline mutation operations.
 *
 * Implements a durable offline outbox queue to track CREATE, UPDATE, and DELETE operations
 * across entities (contacts, safe places, assistance requests, incident reports) until
 * network connectivity is restored and sync completes successfully.
 *
 * @property id Auto-generated numeric primary key.
 * @property entityType Type of domain entity being mutated (CONTACT, PLACE, REQUEST, REPORT).
 * @property entityId Unique string identifier of the referenced entity.
 * @property operation Operation type to replay against the server (CREATE, UPDATE, DELETE).
 * @property payload Serialised JSON representation of payload data.
 * @property createdAt Epoch millisecond timestamp when the sync task was enqueued.
 * @property retryCount Number of times sync execution has been attempted.
 * @property lastAttemptAt Epoch millisecond timestamp of the last attempt.
 * @property status Current sync operation state (PENDING, IN_PROGRESS, FAILED, COMPLETED).
 * @property failureReason Error diagnostics or exception trace if the last attempt failed.
 */
@Entity(tableName = "pending_sync")
data class PendingSyncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val entityType: String,
    val entityId: String,
    val operation: String,
    val payload: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val retryCount: Int = 0,
    val lastAttemptAt: Long? = null,
    val status: String,
    val failureReason: String? = null
)
