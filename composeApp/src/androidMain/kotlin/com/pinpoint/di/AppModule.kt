package com.pinpoint.di

import android.content.Context
import com.pinpoint.util.LocationHelper
import com.pinpoint.domain.repository.FirebaseLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocationHelper(@ApplicationContext context: Context): LocationHelper {
        return LocationHelper(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseLocationRepository(): FirebaseLocationRepository {
        return FirebaseLocationRepository()
    }
}
