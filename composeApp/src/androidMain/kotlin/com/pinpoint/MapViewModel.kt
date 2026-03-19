package com.pinpoint

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pinpoint.data.FirebaseLocationRepository
import com.pinpoint.map.MapScreenSideEffect
import com.pinpoint.map.MapScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val application: Application,
    private val locationRepository: FirebaseLocationRepository,
    private val locationHelper: LocationHelper
) : ContainerHost<MapScreenState, MapScreenSideEffect>, ViewModel() {

    override val container: Container<MapScreenState, MapScreenSideEffect> =
        container(initialState = MapScreenState()) {
            val user = Firebase.auth.currentUser
            if (user != null) {
                reduce { state.copy(currentUserId = user.uid) }
            }
            startTrackingIfPermitted()
            observeGroupMembers()
        }

    private fun startTrackingIfPermitted() = intent {
        val hasPermission = ContextCompat.checkSelfPermission(
            application.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            locationHelper.startLocationUpdates()
            reduce { state.copy(isTracking = true) }
        }
        observeMyLocation()
    }

    private fun observeMyLocation() = intent {
        locationHelper.currentLocation.collectLatest { location ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                reduce { state.copy(myLocation = latLng) }
                updateDistanceToNearest(latLng)
                pushLocationToFirebase(it.latitude, it.longitude)
            }
        }
    }

    private fun pushLocationToFirebase(lat: Double, lng: Double) {
        val user = Firebase.auth.currentUser ?: return
        locationRepository.updateMyLocation(
            uid = user.uid,
            displayName = user.displayName ?: "Unknown",
            lat = lat,
            lng = lng
        )
    }

    private fun observeGroupMembers() = intent {
        locationRepository.observeMembers().collectLatest { members ->
            reduce { state.copy(members = members) }
            state.myLocation?.let { updateDistanceToNearest(it) }
        }
    }

    fun onPermissionResult(granted: Boolean) = intent {
        if (granted) {
            locationHelper.startLocationUpdates()
            reduce { state.copy(isTracking = true) }
        }
    }

    private fun updateDistanceToNearest(myLocation: LatLng) = intent {
        val otherMembers = state.members.filter { it.uid != state.currentUserId }
        if (otherMembers.isEmpty()) {
            reduce { state.copy(distance = "No other members nearby") }
            return@intent
        }
        val nearest = otherMembers.minByOrNull { member ->
            val results = FloatArray(1)
            Location.distanceBetween(
                myLocation.latitude, myLocation.longitude,
                member.latitude, member.longitude,
                results
            )
            results[0]
        } ?: return@intent
        val results = FloatArray(1)
        Location.distanceBetween(
            myLocation.latitude, myLocation.longitude,
            nearest.latitude, nearest.longitude,
            results
        )
        val distanceInMeters = results[0]
        val distanceText = if (distanceInMeters >= 1000) {
            String.format("%.2f km to %s", distanceInMeters / 1000, nearest.displayName)
        } else {
            String.format("%.0f m to %s", distanceInMeters, nearest.displayName)
        }
        reduce { state.copy(distance = distanceText) }
    }

    override fun onCleared() {
        super.onCleared()
        locationHelper.stopLocationUpdates()
        val user = Firebase.auth.currentUser
        user?.let { locationRepository.removeMyLocation(it.uid) }
    }
}