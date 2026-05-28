package com.pinpoint.feature.map

sealed interface MapScreenSideEffect {
    data class ShowError(val message: String) : MapScreenSideEffect
    data class ShowSuccess(val message: String) : MapScreenSideEffect
    data class NavigateToGroupDetail(val groupId: String, val groupName: String) : MapScreenSideEffect
}
