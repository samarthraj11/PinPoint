package com.pinpoint.feature.group

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.ramcosta.composedestinations.generated.destinations.MapScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RootGraph>
@Composable
fun GroupsScreen(
    navigator: DestinationsNavigator,
    viewModel: GroupsViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    // Back press navigates to Map instead of closing app
    BackHandler {
        navigator.navigate(MapScreenDestination) {
            popUpTo(MapScreenDestination) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is GroupsScreenSideEffect.NavigateToGroupDetail -> {
                navigator.navigate(GroupDetailScreenDestination(sideEffect.groupId, sideEffect.groupName))
            }
            is GroupsScreenSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is GroupsScreenSideEffect.ShowSuccess -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        containerColor = LocalColors.BackgroundDark,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            PinPointBottomBar(
                currentTab = BottomTab.Groups,
                navigator = navigator
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LocalColors.BackgroundDark)
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColors.BackgroundDark.copy(alpha = 0.8f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trip Groups",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalColors.TextPrimary,
                        letterSpacing = (-0.5).sp
                    )
                }
            }

            // Content
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LocalColors.Primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Action buttons
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { viewModel.showCreateDialog() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LocalColors.Primary
                                )
                            ) {
                                Text(
                                    text = "Create Group",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = LocalColors.White
                                )
                            }
                            OutlinedButton(
                                onClick = { viewModel.showJoinDialog() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = LocalColors.Primary
                                ),
                                border = ButtonDefaults.outlinedButtonBorder(true).copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(LocalColors.Primary)
                                )
                            ) {
                                Text(
                                    text = "Join Group",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    if (state.groups.isEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            EmptyGroupsCard()
                        }
                    } else {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Your Groups",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalColors.TextPrimary
                            )
                        }

                        items(state.groups, key = { it.id }) { group ->
                            GroupCard(
                                group = group,
                                onClick = { viewModel.onGroupClick(group.id, group.name) }
                            )
                        }
                    }
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
private fun GroupCard(group: Group, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.SurfaceDark
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderLight)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Group avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LocalColors.PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name.take(2).uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.Primary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${group.memberCount} member${if (group.memberCount != 1) "s" else ""}",
                    fontSize = 13.sp,
                    color = LocalColors.TextSecondary
                )
            }

            // Member avatars stack
            Row(modifier = Modifier.padding(end = 8.dp)) {
                val displayMembers = group.members.values.take(3)
                displayMembers.forEachIndexed { index, member ->
                    Box(
                        modifier = Modifier
                            .offset(x = (-8 * index).dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when (index) {
                                    0 -> LocalColors.Primary
                                    1 -> LocalColors.Orange300
                                    else -> LocalColors.Orange200
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.displayName.take(1).uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalColors.White
                        )
                    }
                }
                if (group.memberCount > 3) {
                    Box(
                        modifier = Modifier
                            .offset(x = (-8 * displayMembers.size).dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(LocalColors.SurfaceDarkElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${group.memberCount - 3}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalColors.TextSecondary
                        )
                    }
                }
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
private fun EmptyGroupsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.SurfaceDark.copy(alpha = 0.5f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderLight)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No groups yet",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create a new group or join an existing one to start sharing locations.",
                fontSize = 14.sp,
                color = LocalColors.TextSecondary,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
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
        title = {
            Text(text = "Create Group", fontWeight = FontWeight.Bold)
        },
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
        title = {
            Text(text = "Join Group", fontWeight = FontWeight.Bold)
        },
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
