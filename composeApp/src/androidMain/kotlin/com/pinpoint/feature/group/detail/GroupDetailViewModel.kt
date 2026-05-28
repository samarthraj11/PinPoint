package com.pinpoint.feature.group.detail

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pinpoint.domain.repository.FirebaseLocationRepository
import com.pinpoint.util.LocationHelper
import com.ramcosta.composedestinations.generated.destinations.GroupDetailScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val locationRepository: FirebaseLocationRepository,
    private val locationHelper: LocationHelper
) : ContainerHost<GroupDetailScreenState, GroupDetailSideEffect>, ViewModel() {

    private val navArgs = GroupDetailScreenDestination.argsFrom(savedStateHandle)

    override val container: Container<GroupDetailScreenState, GroupDetailSideEffect> =
        container(initialState = GroupDetailScreenState()) {
            val user = Firebase.auth.currentUser
            reduce {
                state.copy(
                    groupId = navArgs.groupId,
                    groupName = navArgs.groupName,
                    currentUserId = user?.uid ?: "",
                    currentUserName = user?.displayName ?: "",
                    currentUserPhoto = user?.photoUrl?.toString()
                )
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
            lng = lng,
            groupId = navArgs.groupId
        )
    }

    private fun observeGroupMembers() = intent {
        locationRepository.observeMembers(navArgs.groupId).collectLatest { members ->
            reduce { state.copy(members = members) }
        }
    }

    fun onPermissionResult(granted: Boolean) = intent {
        if (granted) {
            locationHelper.startLocationUpdates()
            reduce { state.copy(isTracking = true) }
        }
    }

    fun onGroupIdCopied() = intent {
        reduce { state.copy(showGroupIdCopied = true) }
    }

    override fun onCleared() {
        super.onCleared()
        locationHelper.stopLocationUpdates()
        val user = Firebase.auth.currentUser
        user?.let { locationRepository.removeMyLocation(it.uid, container.stateFlow.value.groupId) }
    }
}
