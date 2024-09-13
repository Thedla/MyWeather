package com.example.myweather.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.myweather.datastore.CityDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// Extension property to create DataStore instance
val Context.dataStore by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): CityDataStore {
        return CityDataStore(context.dataStore)
    }
}
