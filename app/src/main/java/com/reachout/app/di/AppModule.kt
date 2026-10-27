package com.reachout.app.di

import android.content.Context
import com.reachout.app.data.connectivity.ConnectivityObserver
import com.reachout.app.data.connectivity.NetworkConnectivityObserver
import com.reachout.app.data.repository.AssistanceRequestRepositoryImpl
import com.reachout.app.data.repository.ContactRepositoryImpl
import com.reachout.app.data.repository.EmergencyGuideRepositoryImpl
import com.reachout.app.data.repository.PlaceRepositoryImpl
import com.reachout.app.domain.repository.AssistanceRequestRepository
import com.reachout.app.domain.repository.ContactRepository
import com.reachout.app.domain.repository.EmergencyGuideRepository
import com.reachout.app.domain.repository.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Main application Hilt module binding repository implementations to their interfaces
 * and providing application-level dependencies like [ConnectivityObserver].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Binds [ContactRepositoryImpl] to [ContactRepository].
     */
    @Binds
    @Singleton
    abstract fun bindContactRepository(
        impl: ContactRepositoryImpl
    ): ContactRepository

    /**
     * Binds [PlaceRepositoryImpl] to [PlaceRepository].
     */
    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        impl: PlaceRepositoryImpl
    ): PlaceRepository

    /**
     * Binds [AssistanceRequestRepositoryImpl] to [AssistanceRequestRepository].
     */
    @Binds
    @Singleton
    abstract fun bindAssistanceRequestRepository(
        impl: AssistanceRequestRepositoryImpl
    ): AssistanceRequestRepository

    /**
     * Binds [EmergencyGuideRepositoryImpl] to [EmergencyGuideRepository].
     */
    @Binds
    @Singleton
    abstract fun bindEmergencyGuideRepository(
        impl: EmergencyGuideRepositoryImpl
    ): EmergencyGuideRepository

    companion object {
        /**
         * Provides the singleton [ConnectivityObserver] for observing network state changes.
         *
         * @param context Application context.
         * @return [NetworkConnectivityObserver] instance.
         */
        @Provides
        @Singleton
        fun provideConnectivityObserver(
            @ApplicationContext context: Context
        ): ConnectivityObserver {
            return NetworkConnectivityObserver(context)
        }
    }
}
