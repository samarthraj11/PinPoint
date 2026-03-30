package com.pinpoint.feature.group.detail

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.pinpoint.domain.model.MemberLocation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RootGraph>
@Composable
fun GroupDetailScreen(
    groupId: String,
    groupName: String,
    navigator: DestinationsNavigator,
    viewModel: GroupDetailViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is GroupDetailSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is GroupDetailSideEffect.NavigateBack -> {
                navigator.popBackStack()
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

    val defaultPosition = LatLng(28.6139, 77.2090)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultPosition, 12f)
    }

    val allPositions = buildList {
        state.myLocation?.let { add(it) }
        state.members
            .filter { it.uid != state.currentUserId }
            .forEach { add(it.toLatLng()) }
    }

    LaunchedEffect(state.myLocation, state.members) {
        if (allPositions.size >= 2) {
            val bounds = LatLngBounds.builder()
            allPositions.forEach { bounds.include(it) }
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds.build(), 100)
            )
        } else if (allPositions.size == 1) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(allPositions.first(), 15f)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.BackgroundDark)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalColors.BackgroundDark.copy(alpha = 0.9f))
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigator.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LocalColors.TextPrimary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.groupName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${state.members.size} member${if (state.members.size != 1) "s" else ""} active",
                    fontSize = 12.sp,
                    color = LocalColors.TextSecondary
                )
            }
            // Share group ID button
            IconButton(onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Group ID", state.groupId))
                viewModel.onGroupIdCopied()
                Toast.makeText(context, "Group ID copied!", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Group ID",
                    tint = LocalColors.Primary
                )
            }
        }

        // Group ID banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalColors.PrimaryDim)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Group ID:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.Primary,
                letterSpacing = 1.sp
            )
            Text(
                text = state.groupId,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.Primary
            )
        }

        // Map
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false),
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                state.myLocation?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "You",
                        snippet = "Your current location"
                    )
                }

                state.members
                    .filter { it.uid != state.currentUserId }
                    .forEach { member ->
                        Marker(
                            state = MarkerState(position = member.toLatLng()),
                            title = member.displayName,
                            snippet = "Group member"
                        )

                        state.myLocation?.let { my ->
                            Polyline(
                                points = listOf(my, member.toLatLng()),
                                color = LocalColors.Primary,
                                width = 4f
                            )
                        }
                    }
            }

            // Live badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(
                        color = LocalColors.PrimaryLight,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "LIVE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalColors.Primary,
                    letterSpacing = 1.5.sp
                )
            }
        }

        // Members list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .background(
                    LocalColors.SurfaceDark,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nearby Members",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalColors.Primary,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "${state.members.size} online",
                    fontSize = 12.sp,
                    color = LocalColors.TextSecondary
                )
            }

            HorizontalDivider(color = LocalColors.BorderLight)

            if (state.members.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No members online yet. Share the group ID to invite others.",
                        fontSize = 14.sp,
                        color = LocalColors.TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.members, key = { it.uid }) { member ->
                        MemberRow(
                            member = member,
                            isCurrentUser = member.uid == state.currentUserId,
                            myLocation = state.myLocation
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberRow(
    member: MemberLocation,
    isCurrentUser: Boolean,
    myLocation: LatLng?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (isCurrentUser) LocalColors.Primary else LocalColors.SurfaceDarkElevated
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.displayName.take(1).uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentUser) LocalColors.White else LocalColors.TextPrimary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = member.displayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary
                )
                if (isCurrentUser) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "You",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalColors.Primary,
                        modifier = Modifier
                            .background(
                                LocalColors.PrimaryDim,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        if (!isCurrentUser && myLocation != null) {
            val results = FloatArray(1)
            android.location.Location.distanceBetween(
                myLocation.latitude, myLocation.longitude,
                member.latitude, member.longitude,
                results
            )
            val distance = results[0]
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (distance >= 1000) {
                        String.format("%.1f km", distance / 1000)
                    } else {
                        String.format("%.0f m", distance)
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.Primary
                )
                Text(
                    text = "AWAY",
                    fontSize = 9.sp,
                    color = LocalColors.TextMuted,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
