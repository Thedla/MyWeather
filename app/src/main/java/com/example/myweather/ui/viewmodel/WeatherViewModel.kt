package com.example.myweather.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.BuildConfig
import com.example.myweather.data.model.Resource
import com.example.myweather.data.model.WeatherResponse
import com.example.myweather.data.repository.WeatherRepository
import com.example.myweather.datastore.CityDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val cityDataStore: CityDataStore
) : ViewModel() {

    private val _weatherState = MutableStateFlow<Resource<WeatherResponse>>(Resource.Loading)
    val weatherState: StateFlow<Resource<WeatherResponse>> = _weatherState

    private val _lastSearchedCity = MutableStateFlow<String?>(null)
    val lastSearchedCity: StateFlow<String?> = _lastSearchedCity


    fun saveLastSearchedCity(city: String) {
        viewModelScope.launch {
            cityDataStore.saveCity(city)
        }
    }

    fun loadLastSearchedCity() {
        viewModelScope.launch {
            cityDataStore.getCity().collect { city ->
                _lastSearchedCity.value = city
                city?.let {
                    fetchWeather(it, BuildConfig.WEATHER_API_KEY) // Automatically fetch weather
                }
            }
        }
    }

    fun fetchWeather(cityName: String, apiKey: String) {
        viewModelScope.launch {
            _weatherState.value = Resource.Loading
            val result = repository.getWeatherByCity(cityName, apiKey)
            _weatherState.value = result
        }
    }

    fun fetchWeatherByCoordinates(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            _weatherState.value = Resource.Loading
            val result = repository.getWeatherByCoordinates(lat, lon, apiKey)
            _weatherState.value = result
        }
    }
}
