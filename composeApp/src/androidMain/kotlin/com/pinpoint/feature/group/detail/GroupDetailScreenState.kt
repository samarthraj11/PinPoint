package com.pinpoint.feature.group.detail

import com.google.android.gms.maps.model.LatLng
import com.pinpoint.domain.model.MemberLocation

data class GroupDetailScreenState(
    val groupId: String = "",
    val groupName: String = "",
    val members: List<MemberLocation> = emptyList(),
    val myLocation: LatLng? = null,
    val currentUserId: String = "",
    val currentUserName: String = "",
    val currentUserPhoto: String? = null,
    val isTracking: Boolean = false,
    val showGroupIdCopied: Boolean = false
)
