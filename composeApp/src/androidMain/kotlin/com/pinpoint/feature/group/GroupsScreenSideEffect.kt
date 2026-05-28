package com.pinpoint.feature.group

sealed interface GroupsScreenSideEffect {
    data class NavigateToGroupDetail(val groupId: String, val groupName: String) : GroupsScreenSideEffect
    data class ShowError(val message: String) : GroupsScreenSideEffect
    data class ShowSuccess(val message: String) : GroupsScreenSideEffect
}
