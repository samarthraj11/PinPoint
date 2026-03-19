package com.pinpoint.map

sealed interface MapScreenSideEffect {
    data class ShowError(val message: String) : MapScreenSideEffect
}
