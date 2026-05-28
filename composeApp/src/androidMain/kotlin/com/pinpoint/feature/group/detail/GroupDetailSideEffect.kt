package com.pinpoint.feature.group.detail

sealed interface GroupDetailSideEffect {
    data class ShowError(val message: String) : GroupDetailSideEffect
    data object NavigateBack : GroupDetailSideEffect
}
