package com.pinpoint.feature.map

sealed interface MapScreenSideEffect {
    data class ShowError(val message: String) : MapScreenSideEffect
}
