package com.pinpoint.feature.map

import com.google.android.gms.maps.model.LatLng
import com.pinpoint.domain.model.MemberLocation
import com.pinpoint.domain.repository.FirebaseLocationRepository

data class MapScreenState(
    val myLocation: LatLng? = null,
    val members: List<MemberLocation> = emptyList(),
    val isTracking: Boolean = false,
    val currentUserId: String = "",
    val distance: String = "Calculating...",
    val groupId: String = FirebaseLocationRepository.DEFAULT_GROUP_ID
)
