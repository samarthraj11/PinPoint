package com.pinpoint.data.model

import com.google.android.gms.maps.model.LatLng

data class MemberLocation(
    val uid: String = "",
    val displayName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Long = 0L
) {
    fun toLatLng(): LatLng = LatLng(latitude, longitude)
}
