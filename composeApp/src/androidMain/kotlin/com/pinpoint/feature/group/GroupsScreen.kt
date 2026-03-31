package com.pinpoint.feature.group

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.pinpoint.design.BottomTab
import com.pinpoint.design.PinPointBottomBar
import com.pinpoint.design.composable.CreateGroupDialogComposable
import com.pinpoint.design.composable.JoinGroupDialogComposable
import com.pinpoint.domain.model.Group
import com.pinpoint.feature.group.composable.EmptyGroupsCardComposable
import com.pinpoint.feature.group.composable.GroupCardComposable
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
                            EmptyGroupsCardComposable()
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
                            GroupCardComposable(
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

