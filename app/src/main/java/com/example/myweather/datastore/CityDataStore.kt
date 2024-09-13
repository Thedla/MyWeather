package com.example.myweather.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityDataStore(private val dataStore: DataStore<Preferences>) {

    private val CITY_KEY = stringPreferencesKey("last_searched_city")

    suspend fun saveCity(city: String) {
        dataStore.edit { preferences ->
            preferences[CITY_KEY] = city
        }
    }


    fun getCity(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[CITY_KEY]
        }
    }
}

