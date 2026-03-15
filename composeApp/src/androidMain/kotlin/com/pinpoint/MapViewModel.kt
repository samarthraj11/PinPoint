package com.pinpoint

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val locationHelper = LocationHelper(application.applicationContext)

    // Our location
    private val _myLocation = MutableStateFlow<LatLng?>(null)
    val myLocation: StateFlow<LatLng?> = _myLocation

    // Other person's location (simulated or from Firebase)
    private val _otherLocation = MutableStateFlow(
        LatLng(28.6139, 77.2090) // Example: New Delhi (replace with real data)
    )
    val otherLocation: StateFlow<LatLng> = _otherLocation

    // Distance between us
    private val _distance = MutableStateFlow("Calculating...")
    val distance: StateFlow<String> = _distance

    init {
        val context = application.applicationContext
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            locationHelper.startLocationUpdates()
        }

        viewModelScope.launch {
            locationHelper.currentLocation.collectLatest { location ->
                println("karl : $location")
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    _myLocation.value = latLng
                    calculateDistance(latLng, _otherLocation.value)
                }
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            locationHelper.startLocationUpdates()
        }
    }

    private fun calculateDistance(from: LatLng, to: LatLng) {
        val results = FloatArray(1)
        Location.distanceBetween(
            from.latitude, from.longitude,
            to.latitude, to.longitude,
            results
        )

        val distanceInMeters = results[0]
        _distance.value = if (distanceInMeters >= 1000) {
            String.format("%.2f km", distanceInMeters / 1000)
        } else {
            String.format("%.0f meters", distanceInMeters)
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationHelper.stopLocationUpdates()
    }
}