package com.pinpoint.feature.map

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.pinpoint.design.BottomTab
import com.pinpoint.design.PinPointBottomBar
import com.pinpoint.design.PinPointTheme
import com.pinpoint.design.composable.CreateGroupDialogComposable
import com.pinpoint.design.composable.JoinGroupDialogComposable
import com.pinpoint.domain.model.Group
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
        MapScreenContent(
            state = state,
            innerPadding = innerPadding,
            onCreateGroup = viewModel::showCreateDialog,
            onJoinGroup = viewModel::showJoinDialog,
            onGroupClick = { group ->
                navigator.navigate(GroupDetailScreenDestination(group.id, group.name))
            },
            onCreateGroupNameChange = viewModel::onCreateGroupNameChange,
            onConfirmCreateGroup = viewModel::createGroup,
            onDismissCreateDialog = viewModel::hideCreateDialog,
            onJoinGroupIdChange = viewModel::onJoinGroupIdChange,
            onConfirmJoinGroup = viewModel::joinGroup,
            onDismissJoinDialog = viewModel::hideJoinDialog
        )
    }
}

@Composable
internal fun MapScreenContent(
    state: MapScreenState,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    onCreateGroup: () -> Unit,
    onJoinGroup: () -> Unit,
    onGroupClick: (Group) -> Unit,
    onCreateGroupNameChange: (String) -> Unit,
    onConfirmCreateGroup: () -> Unit,
    onDismissCreateDialog: () -> Unit,
    onJoinGroupIdChange: (String) -> Unit,
    onConfirmJoinGroup: () -> Unit,
    onDismissJoinDialog: () -> Unit
) {
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
            state.hasGroups.not() -> {
                NoGroupsPlaceholderComposable(
                    onCreateGroup = onCreateGroup,
                    onJoinGroup = onJoinGroup
                )
            }
            else -> {
                GroupsListContentComposable(
                    groups = state.groups,
                    onGroupClick = onGroupClick
                )
            }
        }
    }

    if (state.showCreateDialog) {
        CreateGroupDialogComposable(
            groupName = state.createGroupName,
            onGroupNameChange = onCreateGroupNameChange,
            onConfirm = onConfirmCreateGroup,
            onDismiss = onDismissCreateDialog,
            isLoading = state.isCreating
        )
    }

    if (state.showJoinDialog) {
        JoinGroupDialogComposable(
            groupId = state.joinGroupId,
            onGroupIdChange = onJoinGroupIdChange,
            onConfirm = onConfirmJoinGroup,
            onDismiss = onDismissJoinDialog,
            isLoading = state.isJoining
        )
    }
}

// ─── Previews ───────────────────────────────────────────────────────────────

private val previewGroups = listOf(
    Group(id = "1", name = "Weekend Trip", createdBy = "uid_abc", createdByName = "Samarth", memberCount = 4),
    Group(id = "2", name = "Family", createdBy = "uid_abc", createdByName = "Samarth", memberCount = 7),
    Group(id = "3", name = "Work Crew", createdBy = "uid_abc", createdByName = "Samarth", memberCount = 2)
)

@Preview(showBackground = true, name = "Map Screen — Loading")
@Composable
private fun PreviewMapScreenLoading() {
    PinPointTheme {
        MapScreenContent(
            state = MapScreenState(isLoadingGroups = true),
            onCreateGroup = {},
            onJoinGroup = {},
            onGroupClick = {},
            onCreateGroupNameChange = {},
            onConfirmCreateGroup = {},
            onDismissCreateDialog = {},
            onJoinGroupIdChange = {},
            onConfirmJoinGroup = {},
            onDismissJoinDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Map Screen — No Groups")
@Composable
private fun PreviewMapScreenNoGroups() {
    PinPointTheme {
        MapScreenContent(
            state = MapScreenState(isLoadingGroups = false, groups = emptyList()),
            onCreateGroup = {},
            onJoinGroup = {},
            onGroupClick = {},
            onCreateGroupNameChange = {},
            onConfirmCreateGroup = {},
            onDismissCreateDialog = {},
            onJoinGroupIdChange = {},
            onConfirmJoinGroup = {},
            onDismissJoinDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Map Screen — With Groups")
@Composable
private fun PreviewMapScreenWithGroups() {
    PinPointTheme {
        MapScreenContent(
            state = MapScreenState(isLoadingGroups = false, groups = previewGroups),
            onCreateGroup = {},
            onJoinGroup = {},
            onGroupClick = {},
            onCreateGroupNameChange = {},
            onConfirmCreateGroup = {},
            onDismissCreateDialog = {},
            onJoinGroupIdChange = {},
            onConfirmJoinGroup = {},
            onDismissJoinDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Map Screen — Create Group Dialog")
@Composable
private fun PreviewMapScreenCreateDialog() {
    PinPointTheme {
        MapScreenContent(
            state = MapScreenState(
                isLoadingGroups = false,
                groups = emptyList(),
                showCreateDialog = true,
                createGroupName = "Weekend Trip",
                isCreating = false
            ),
            onCreateGroup = {},
            onJoinGroup = {},
            onGroupClick = {},
            onCreateGroupNameChange = {},
            onConfirmCreateGroup = {},
            onDismissCreateDialog = {},
            onJoinGroupIdChange = {},
            onConfirmJoinGroup = {},
            onDismissJoinDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Map Screen — Join Group Dialog")
@Composable
private fun PreviewMapScreenJoinDialog() {
    PinPointTheme {
        MapScreenContent(
            state = MapScreenState(
                isLoadingGroups = false,
                groups = emptyList(),
                showJoinDialog = true,
                joinGroupId = "-NxK2abc",
                isJoining = false
            ),
            onCreateGroup = {},
            onJoinGroup = {},
            onGroupClick = {},
            onCreateGroupNameChange = {},
            onConfirmCreateGroup = {},
            onDismissCreateDialog = {},
            onJoinGroupIdChange = {},
            onConfirmJoinGroup = {},
            onDismissJoinDialog = {}
        )
    }
}
