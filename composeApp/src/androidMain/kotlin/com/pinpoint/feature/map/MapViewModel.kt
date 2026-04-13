package com.pinpoint.feature.map

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pinpoint.domain.repository.FirebaseGroupRepository
import com.pinpoint.domain.repository.FirebaseLocationRepository
import com.pinpoint.util.LocationHelper
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
    private val locationHelper: LocationHelper,
    private val groupRepository: FirebaseGroupRepository
) : ContainerHost<MapScreenState, MapScreenSideEffect>, ViewModel() {

    override val container: Container<MapScreenState, MapScreenSideEffect> =
        container(initialState = MapScreenState()) {
            val user = Firebase.auth.currentUser
            if (user != null) {
                reduce {
                    state.copy(
                        currentUserId = user.uid,
                        currentUserDisplayName = user.displayName ?: "",
                        currentUserPhotoUrl = user.photoUrl?.toString()
                    )
                }
                observeUserGroups(user.uid)
            }
            startTrackingIfPermitted()
        }

    private fun observeUserGroups(uid: String) = intent {
        groupRepository.observeUserGroups(uid).collectLatest { groups ->
            reduce { state.copy(groups = groups, isLoadingGroups = false) }
        }
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
            }
        }
    }

    fun onPermissionResult(granted: Boolean) = intent {
        if (granted) {
            locationHelper.startLocationUpdates()
            reduce { state.copy(isTracking = true) }
        }
    }

    fun showCreateDialog() = intent {
        reduce { state.copy(showCreateDialog = true, createGroupName = "") }
    }

    fun hideCreateDialog() = intent {
        reduce { state.copy(showCreateDialog = false) }
    }

    fun showJoinDialog() = intent {
        reduce { state.copy(showJoinDialog = true, joinGroupId = "") }
    }

    fun hideJoinDialog() = intent {
        reduce { state.copy(showJoinDialog = false) }
    }

    fun onCreateGroupNameChange(name: String) = intent {
        reduce { state.copy(createGroupName = name) }
    }

    fun onJoinGroupIdChange(id: String) = intent {
        reduce { state.copy(joinGroupId = id) }
    }

    fun createGroup() = intent {
        val name = state.createGroupName.trim()
        if (name.isEmpty()) {
            postSideEffect(MapScreenSideEffect.ShowError("Group name cannot be empty"))
            return@intent
        }
        reduce { state.copy(isCreating = true) }
        try {
            val groupId = groupRepository.createGroup(
                name = name,
                creatorUid = state.currentUserId,
                creatorName = state.currentUserDisplayName,
                creatorPhotoUrl = state.currentUserPhotoUrl
            )
            reduce { state.copy(isCreating = false, showCreateDialog = false) }
            postSideEffect(MapScreenSideEffect.ShowSuccess("Group created!"))
            postSideEffect(MapScreenSideEffect.NavigateToGroupDetail(groupId, name))
        } catch (e: Exception) {
            reduce { state.copy(isCreating = false) }
            postSideEffect(MapScreenSideEffect.ShowError("Failed to create group: ${e.message}"))
        }
    }

    fun joinGroup() = intent {
        val groupId = state.joinGroupId.trim()
        if (groupId.isEmpty()) {
            postSideEffect(MapScreenSideEffect.ShowError("Group ID cannot be empty"))
            return@intent
        }
        reduce { state.copy(isJoining = true) }
        try {
            val success = groupRepository.joinGroup(
                groupId = groupId,
                uid = state.currentUserId,
                displayName = state.currentUserDisplayName,
                photoUrl = state.currentUserPhotoUrl
            )
            if (success) {
                reduce { state.copy(isJoining = false, showJoinDialog = false) }
                postSideEffect(MapScreenSideEffect.ShowSuccess("Joined group!"))
            } else {
                reduce { state.copy(isJoining = false) }
                postSideEffect(MapScreenSideEffect.ShowError("Group not found"))
            }
        } catch (e: Exception) {
            reduce { state.copy(isJoining = false) }
            postSideEffect(MapScreenSideEffect.ShowError("Failed to join group: ${e.message}"))
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationHelper.stopLocationUpdates()
    }
}
