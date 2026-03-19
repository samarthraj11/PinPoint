package com.pinpoint.map

import com.google.android.gms.maps.model.LatLng
import com.pinpoint.data.model.MemberLocation

data class MapScreenState(
    val myLocation: LatLng? = null,
    val members: List<MemberLocation> = emptyList(),
    val isTracking: Boolean = false,
    val currentUserId: String = "",
    val distance: String = "Calculating..."
)
