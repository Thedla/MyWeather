package com.example.myweather.data.repository

import com.example.myweather.data.model.Resource
import com.example.myweather.data.model.WeatherResponse
import com.example.myweather.data.service.WeatherApi
import com.example.myweather.data.service.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {

    // Fetch weather by city name using the common error handler
    suspend fun getWeatherByCity(cityName: String, apiKey: String): Resource<WeatherResponse> {
        return safeApiCall {
            api.getWeatherByCity(cityName, apiKey)
        }
    }

    // Fetch weather by coordinates using the common error handler
    suspend fun getWeatherByCoordinates(latitude: Double, longitude: Double, apiKey: String): Resource<WeatherResponse> {
        return safeApiCall {
            api.getWeatherByCoordinates(latitude, longitude, apiKey)
        }
    }
}