package com.example.myweather.ui.screens


import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myweather.BuildConfig
import com.example.myweather.data.model.Resource
import com.example.myweather.ui.viewmodel.WeatherViewModel
import com.example.myweather.util.GetCurrentLocation

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    var city by remember { mutableStateOf("") }
    val weatherState by viewModel.weatherState.collectAsState()
    val lastSearchedCity by viewModel.lastSearchedCity.collectAsState()
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    GetCurrentLocation { location ->
        currentLocation = location
    }

    LaunchedEffect(Unit) {
        viewModel.loadLastSearchedCity()
    }

    LaunchedEffect(key1 = lastSearchedCity, key2 = currentLocation) {
        if (lastSearchedCity.isNullOrEmpty() && currentLocation != null) {
            viewModel.fetchWeatherByCoordinates(
                currentLocation!!.latitude,
                currentLocation!!.longitude,
                BuildConfig.WEATHER_API_KEY
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Weather App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                if (city.isNotEmpty()) {
                    viewModel.fetchWeather(city, BuildConfig.WEATHER_API_KEY)
                    viewModel.saveLastSearchedCity(city)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(24.dp))


        when (weatherState) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }
            is Resource.Success -> {
                val weather = (weatherState as Resource.Success).data
                WeatherCard(weather = weather)
                if (lastSearchedCity == null) {
                    viewModel.saveLastSearchedCity(city)
                }
            }
            is Resource.Error -> {
                Text(
                    text = "Error: ${(weatherState as Resource.Error).message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
