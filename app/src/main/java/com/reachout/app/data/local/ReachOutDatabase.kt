package com.reachout.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reachout.app.data.local.dao.AssistanceRequestDao
import com.reachout.app.data.local.dao.ContactDao
import com.reachout.app.data.local.dao.EmergencyGuideDao
import com.reachout.app.data.local.dao.PendingSyncDao
import com.reachout.app.data.local.dao.PlaceDao
import com.reachout.app.data.local.entity.AssistanceRequestEntity
import com.reachout.app.data.local.entity.ContactEntity
import com.reachout.app.data.local.entity.EmergencyGuideEntity
import com.reachout.app.data.local.entity.PendingSyncEntity
import com.reachout.app.data.local.entity.PlaceEntity
import com.reachout.app.data.local.entity.ServiceCategoryEntity

/**
 * Main Room database for ReachOut offline-first community assistance application.
 *
 * Contains tables for local community contacts, safe physical places, community
 * assistance requests, offline emergency guides, service category classifications,
 * and durable offline sync queue entries.
 */
@Database(
    entities = [
        ContactEntity::class,
        PlaceEntity::class,
        AssistanceRequestEntity::class,
        EmergencyGuideEntity::class,
        ServiceCategoryEntity::class,
        PendingSyncEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class ReachOutDatabase : RoomDatabase() {

    /**
     * Accessor for [ContactDao] community contacts queries and transactions.
     */
    abstract fun contactDao(): ContactDao

    /**
     * Accessor for [PlaceDao] safe locations and facility queries and transactions.
     */
    abstract fun placeDao(): PlaceDao

    /**
     * Accessor for [AssistanceRequestDao] local request persistence and sync queueing.
     */
    abstract fun assistanceRequestDao(): AssistanceRequestDao

    /**
     * Accessor for [EmergencyGuideDao] offline crisis response guides and protocols.
     */
    abstract fun emergencyGuideDao(): EmergencyGuideDao

    /**
     * Accessor for [PendingSyncDao] offline mutation queue management.
     */
    abstract fun pendingSyncDao(): PendingSyncDao

    companion object {
        /**
         * Database name used for persistent SQLite storage.
         */
        const val DATABASE_NAME = "reachout_database"
    }
}
