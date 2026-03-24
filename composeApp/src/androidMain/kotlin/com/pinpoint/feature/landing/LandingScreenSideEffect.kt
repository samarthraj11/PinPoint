package com.pinpoint.feature.landing

sealed interface LandingScreenSideEffect {
    data object NavigateToLogin : LandingScreenSideEffect
    data object NavigateToMap : LandingScreenSideEffect
}
