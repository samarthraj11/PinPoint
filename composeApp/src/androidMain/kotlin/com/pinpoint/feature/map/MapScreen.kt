package com.pinpoint.feature.map

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.pinpoint.design.BottomTab
import com.pinpoint.design.PinPointBottomBar
import com.pinpoint.domain.model.Group
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
                    NoGroupsPlaceholder(
                        onCreateGroup = { viewModel.showCreateDialog() },
                        onJoinGroup = { viewModel.showJoinDialog() }
                    )
                }
                else -> {
                    GroupsListContent(
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
        CreateGroupDialog(
            groupName = state.createGroupName,
            onGroupNameChange = viewModel::onCreateGroupNameChange,
            onConfirm = viewModel::createGroup,
            onDismiss = viewModel::hideCreateDialog,
            isLoading = state.isCreating
        )
    }

    // Join Group Dialog
    if (state.showJoinDialog) {
        JoinGroupDialog(
            groupId = state.joinGroupId,
            onGroupIdChange = viewModel::onJoinGroupIdChange,
            onConfirm = viewModel::joinGroup,
            onDismiss = viewModel::hideJoinDialog,
            isLoading = state.isJoining
        )
    }
}

@Composable
private fun NoGroupsPlaceholder(
    onCreateGroup: () -> Unit,
    onJoinGroup: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.BackgroundDark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(LocalColors.PrimaryDim),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uD83D\uDCCD", // pin emoji
                fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "No Groups Yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Join a group or create a new one to start sharing your location with friends and family.",
            fontSize = 15.sp,
            color = LocalColors.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onCreateGroup,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalColors.Primary
            )
        ) {
            Text(
                text = "Create a Group",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = LocalColors.White
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onJoinGroup,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = LocalColors.Primary
            ),
            border = ButtonDefaults.outlinedButtonBorder(true).copy(
                brush = androidx.compose.ui.graphics.SolidColor(LocalColors.Primary)
            )
        ) {
            Text(
                text = "Join Existing Group",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun GroupsListContent(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.BackgroundDark)
    ) {
        // Header
        Text(
            text = "Your Groups",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColors.TextPrimary,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        Text(
            text = "Tap a group to view members and share your location",
            fontSize = 14.sp,
            color = LocalColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(groups, key = { it.id }) { group ->
                GroupQuickCard(group = group, onClick = { onGroupClick(group) })
            }
        }
    }
}

@Composable
private fun GroupQuickCard(group: Group, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColors.SurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderLight)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LocalColors.PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name.take(2).uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.Primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${group.memberCount} member${if (group.memberCount != 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = LocalColors.TextSecondary
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open group",
                tint = LocalColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun CreateGroupDialog(
    groupName: String,
    onGroupNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LocalColors.SurfaceDark,
        titleContentColor = LocalColors.TextPrimary,
        textContentColor = LocalColors.TextSecondary,
        title = { Text(text = "Create Group", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    text = "Give your group a name. You can invite others by sharing the group ID.",
                    fontSize = 14.sp,
                    color = LocalColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = groupName,
                    onValueChange = onGroupNameChange,
                    label = { Text("Group Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LocalColors.Primary,
                        cursorColor = LocalColors.Primary,
                        focusedLabelColor = LocalColors.Primary,
                        unfocusedBorderColor = LocalColors.BorderMedium,
                        unfocusedLabelColor = LocalColors.TextSecondary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = LocalColors.Primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = LocalColors.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create", color = LocalColors.White)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = LocalColors.TextSecondary)
            }
        }
    )
}

@Composable
private fun JoinGroupDialog(
    groupId: String,
    onGroupIdChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LocalColors.SurfaceDark,
        titleContentColor = LocalColors.TextPrimary,
        textContentColor = LocalColors.TextSecondary,
        title = { Text(text = "Join Group", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    text = "Enter the group ID shared by the group creator.",
                    fontSize = 14.sp,
                    color = LocalColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = groupId,
                    onValueChange = onGroupIdChange,
                    label = { Text("Group ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LocalColors.Primary,
                        cursorColor = LocalColors.Primary,
                        focusedLabelColor = LocalColors.Primary,
                        unfocusedBorderColor = LocalColors.BorderMedium,
                        unfocusedLabelColor = LocalColors.TextSecondary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = LocalColors.Primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = LocalColors.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Join", color = LocalColors.White)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = LocalColors.TextSecondary)
            }
        }
    )
}
