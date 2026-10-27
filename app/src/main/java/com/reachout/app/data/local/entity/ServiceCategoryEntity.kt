package com.reachout.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity representing a service category used to organise contacts and places.
 *
 * Facilitates quick filtering on the offline dashboard and directory search views.
 *
 * @property id Unique category code/identifier (e.g. HOSPITAL, POLICE, AMBULANCE, FIRE).
 * @property name Canonical English name of the category.
 * @property displayNameKey String resource key for localised naming across supported languages.
 * @property iconName Material icon identifier or drawable resource name for UI rendering.
 * @property description Human-readable description of what services fall under this category.
 * @property sortOrder Ordering index for category chips and grid layouts.
 * @property isActive Flag indicating whether this category should be displayed in the UI.
 */
@Entity(tableName = "service_categories")
data class ServiceCategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val displayNameKey: String,
    val iconName: String? = null,
    val description: String? = null,
    val sortOrder: Int = 0,
    val isActive: Boolean = true
)
