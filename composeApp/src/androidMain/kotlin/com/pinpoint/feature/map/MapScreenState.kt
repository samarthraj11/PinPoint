package com.pinpoint.feature.map

import com.google.android.gms.maps.model.LatLng
import com.pinpoint.domain.model.Group
import com.pinpoint.domain.model.MemberLocation

data class MapScreenState(
    val myLocation: LatLng? = null,
    val members: List<MemberLocation> = emptyList(),
    val isTracking: Boolean = false,
    val currentUserId: String = "",
    val currentUserDisplayName: String = "",
    val currentUserPhotoUrl: String? = null,
    val distance: String = "Calculating...",
    val groups: List<Group> = emptyList(),
    val isLoadingGroups: Boolean = true,
    val showCreateDialog: Boolean = false,
    val showJoinDialog: Boolean = false,
    val createGroupName: String = "",
    val joinGroupId: String = "",
    val isCreating: Boolean = false,
    val isJoining: Boolean = false
) {
    val hasGroups: Boolean get() = groups.isNotEmpty()
}
