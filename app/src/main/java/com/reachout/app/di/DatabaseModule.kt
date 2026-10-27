package com.reachout.app.di

import android.content.Context
import androidx.room.Room
import com.reachout.app.data.local.ReachOutDatabase
import com.reachout.app.data.local.dao.AssistanceRequestDao
import com.reachout.app.data.local.dao.ContactDao
import com.reachout.app.data.local.dao.EmergencyGuideDao
import com.reachout.app.data.local.dao.PendingSyncDao
import com.reachout.app.data.local.dao.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing Room database and DAO instances to the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "reachout_database"

    /**
     * Provides the singleton [ReachOutDatabase] instance.
     *
     * @param context Application context.
     * @return Initialized [ReachOutDatabase].
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ReachOutDatabase {
        return Room.databaseBuilder(
            context,
            ReachOutDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Provides the [ContactDao] for community contact database access.
     *
     * @param db The [ReachOutDatabase] instance.
     * @return [ContactDao] instance.
     */
    @Provides
    fun provideContactDao(db: ReachOutDatabase): ContactDao {
        return db.contactDao()
    }

    /**
     * Provides the [PlaceDao] for physical places and facility database access.
     *
     * @param db The [ReachOutDatabase] instance.
     * @return [PlaceDao] instance.
     */
    @Provides
    fun providePlaceDao(db: ReachOutDatabase): PlaceDao {
        return db.placeDao()
    }

    /**
     * Provides the [AssistanceRequestDao] for community assistance requests access.
     *
     * @param db The [ReachOutDatabase] instance.
     * @return [AssistanceRequestDao] instance.
     */
    @Provides
    fun provideAssistanceRequestDao(db: ReachOutDatabase): AssistanceRequestDao {
        return db.assistanceRequestDao()
    }

    /**
     * Provides the [EmergencyGuideDao] for emergency guides access.
     *
     * @param db The [ReachOutDatabase] instance.
     * @return [EmergencyGuideDao] instance.
     */
    @Provides
    fun provideEmergencyGuideDao(db: ReachOutDatabase): EmergencyGuideDao {
        return db.emergencyGuideDao()
    }

    /**
     * Provides the [PendingSyncDao] for offline sync queue access.
     *
     * @param db The [ReachOutDatabase] instance.
     * @return [PendingSyncDao] instance.
     */
    @Provides
    fun providePendingSyncDao(db: ReachOutDatabase): PendingSyncDao {
        return db.pendingSyncDao()
    }
}
