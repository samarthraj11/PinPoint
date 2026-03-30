package com.pinpoint.feature.group

import com.pinpoint.domain.model.Group

data class GroupsScreenState(
    val groups: List<Group> = emptyList(),
    val isLoading: Boolean = true,
    val showCreateDialog: Boolean = false,
    val showJoinDialog: Boolean = false,
    val createGroupName: String = "",
    val joinGroupId: String = "",
    val isCreating: Boolean = false,
    val isJoining: Boolean = false,
    val currentUserId: String = "",
    val currentUserName: String = "",
    val currentUserPhoto: String? = null
)
