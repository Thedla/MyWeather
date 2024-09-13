package com.example.myweather.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun GetCurrentLocation(
    onLocationReceived: (Location?) -> Unit,
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Check for location permissions
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.getLastLocation(onLocationReceived)
    } else {
        // Request permissions (this can be improved using a more robust permission request system)
        ActivityCompat.requestPermissions(
            (context as androidx.activity.ComponentActivity),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
    }
}

@SuppressLint("MissingPermission")
fun FusedLocationProviderClient.getLastLocation(onLocationReceived: (Location?) -> Unit) {
    this.lastLocation.addOnSuccessListener { location: Location? ->
        onLocationReceived(location)
    }
}