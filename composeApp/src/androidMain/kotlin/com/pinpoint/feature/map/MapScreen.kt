package com.pinpoint.feature.map

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.pinpoint.design.BottomTab
import com.pinpoint.design.PinPointBottomBar
import com.pinpoint.design.composable.CreateGroupDialogComposable
import com.pinpoint.design.composable.JoinGroupDialogComposable
import com.pinpoint.feature.map.composable.GroupsListContentComposable
import com.pinpoint.feature.map.composable.NoGroupsPlaceholderComposable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.GroupDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RootGraph>
@Composable
fun MapScreen(
    navigator: DestinationsNavigator,
    viewModel: MapViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MapScreenSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is MapScreenSideEffect.ShowSuccess -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is MapScreenSideEffect.NavigateToGroupDetail -> {
                navigator.navigate(
                    GroupDetailScreenDestination(sideEffect.groupId, sideEffect.groupName)
                )
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        viewModel.onPermissionResult(granted)
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        containerColor = LocalColors.BackgroundDark,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            PinPointBottomBar(
                currentTab = BottomTab.Explore,
                navigator = navigator
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoadingGroups -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = LocalColors.Primary)
                    }
                }
                !state.hasGroups -> {
                    NoGroupsPlaceholderComposable(
                        onCreateGroup = { viewModel.showCreateDialog() },
                        onJoinGroup = { viewModel.showJoinDialog() }
                    )
                }
                else -> {
                    GroupsListContentComposable(
                        groups = state.groups,
                        onGroupClick = { group ->
                            navigator.navigate(
                                GroupDetailScreenDestination(group.id, group.name)
                            )
                        }
                    )
                }
            }
        }
    }

    // Create Group Dialog
    if (state.showCreateDialog) {
        CreateGroupDialogComposable(
            groupName = state.createGroupName,
            onGroupNameChange = viewModel::onCreateGroupNameChange,
            onConfirm = viewModel::createGroup,
            onDismiss = viewModel::hideCreateDialog,
            isLoading = state.isCreating
        )
    }

    // Join Group Dialog
    if (state.showJoinDialog) {
        JoinGroupDialogComposable(
            groupId = state.joinGroupId,
            onGroupIdChange = viewModel::onJoinGroupIdChange,
            onConfirm = viewModel::joinGroup,
            onDismiss = viewModel::hideJoinDialog,
            isLoading = state.isJoining
        )
    }
}

