package com.pinpoint.feature.group

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pinpoint.domain.repository.FirebaseGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    private val groupRepository: FirebaseGroupRepository
) : ContainerHost<GroupsScreenState, GroupsScreenSideEffect>, ViewModel() {

    override val container: Container<GroupsScreenState, GroupsScreenSideEffect> =
        container(initialState = GroupsScreenState()) {
            val user = Firebase.auth.currentUser
            if (user != null) {
                reduce {
                    state.copy(
                        currentUserId = user.uid,
                        currentUserName = user.displayName ?: "",
                        currentUserPhoto = user.photoUrl?.toString()
                    )
                }
                observeGroups(user.uid)
            }
        }

    private fun observeGroups(uid: String) = intent {
        groupRepository.observeUserGroups(uid).collectLatest { groups ->
            reduce { state.copy(groups = groups, isLoading = false) }
        }
    }

    fun showCreateDialog() = intent {
        reduce { state.copy(showCreateDialog = true, createGroupName = "") }
    }

    fun hideCreateDialog() = intent {
        reduce { state.copy(showCreateDialog = false) }
    }

    fun showJoinDialog() = intent {
        reduce { state.copy(showJoinDialog = true, joinGroupId = "") }
    }

    fun hideJoinDialog() = intent {
        reduce { state.copy(showJoinDialog = false) }
    }

    fun onCreateGroupNameChange(name: String) = intent {
        reduce { state.copy(createGroupName = name) }
    }

    fun onJoinGroupIdChange(id: String) = intent {
        reduce { state.copy(joinGroupId = id) }
    }

    fun createGroup() = intent {
        val name = state.createGroupName.trim()
        if (name.isEmpty()) {
            postSideEffect(GroupsScreenSideEffect.ShowError("Group name cannot be empty"))
            return@intent
        }
        reduce { state.copy(isCreating = true) }
        try {
            val groupId = groupRepository.createGroup(
                name = name,
                creatorUid = state.currentUserId,
                creatorName = state.currentUserName,
                creatorPhotoUrl = state.currentUserPhoto
            )
            reduce { state.copy(isCreating = false, showCreateDialog = false) }
            postSideEffect(GroupsScreenSideEffect.ShowSuccess("Group created!"))
            postSideEffect(GroupsScreenSideEffect.NavigateToGroupDetail(groupId, name))
        } catch (e: Exception) {
            reduce { state.copy(isCreating = false) }
            postSideEffect(GroupsScreenSideEffect.ShowError("Failed to create group: ${e.message}"))
        }
    }

    fun joinGroup() = intent {
        val groupId = state.joinGroupId.trim()
        if (groupId.isEmpty()) {
            postSideEffect(GroupsScreenSideEffect.ShowError("Group ID cannot be empty"))
            return@intent
        }
        reduce { state.copy(isJoining = true) }
        try {
            val success = groupRepository.joinGroup(
                groupId = groupId,
                uid = state.currentUserId,
                displayName = state.currentUserName,
                photoUrl = state.currentUserPhoto
            )
            if (success) {
                reduce { state.copy(isJoining = false, showJoinDialog = false) }
                postSideEffect(GroupsScreenSideEffect.ShowSuccess("Joined group!"))
            } else {
                reduce { state.copy(isJoining = false) }
                postSideEffect(GroupsScreenSideEffect.ShowError("Group not found"))
            }
        } catch (e: Exception) {
            reduce { state.copy(isJoining = false) }
            postSideEffect(GroupsScreenSideEffect.ShowError("Failed to join group: ${e.message}"))
        }
    }

    fun onGroupClick(groupId: String, groupName: String) = intent {
        postSideEffect(GroupsScreenSideEffect.NavigateToGroupDetail(groupId, groupName))
    }
}
