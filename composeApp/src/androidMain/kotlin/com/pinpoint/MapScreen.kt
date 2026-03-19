package com.pinpoint

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.pinpoint.map.MapScreenSideEffect

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {

    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MapScreenSideEffect.ShowError -> {
                // Handle error display
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        viewModel.onPermissionResult(granted)
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
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

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            // My Location Marker
            state.myLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You",
                    snippet = "Your current location"
                )
            }

            // Other group members' markers
            state.members
                .filter { it.uid != state.currentUserId }
                .forEach { member ->
                    Marker(
                        state = MarkerState(position = member.toLatLng()),
                        title = member.displayName,
                        snippet = "ID: ${member.uid.take(8)}"
                    )

                    // Draw lines from my location to each member
                    state.myLocation?.let { my ->
                        Polyline(
                            points = listOf(my, member.toLatLng()),
                            color = Color.Blue,
                            width = 6f
                        )
                    }
                }
        }

        // Active Trip Info Card (Top)
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Trip",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${state.members.size} member${if (state.members.size != 1) "s" else ""}",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Distance Card (Bottom)
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📍 Distance",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = state.distance,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (state.members.size > 1) "to nearest group member" else "waiting for members",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}