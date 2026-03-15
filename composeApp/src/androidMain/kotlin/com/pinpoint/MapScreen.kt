package com.pinpoint

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*

@Composable
fun MapScreen(viewModel: MapViewModel = viewModel()) {

    val myLocation by viewModel.myLocation.collectAsState()
    val otherLocation by viewModel.otherLocation.collectAsState()
    val distance by viewModel.distance.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        viewModel.onPermissionResult(granted)  // ← Called here!
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(otherLocation, 12f)
    }

    // Auto-fit camera to show both markers
    LaunchedEffect(myLocation, otherLocation) {
        myLocation?.let { my ->
            val bounds = LatLngBounds.builder()
                .include(my)
                .include(otherLocation)
                .build()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🗺️ Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            // 📍 My Location Marker
            myLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "You",
                    snippet = "Your current location"
                )
            }

            // 📍 Other Person's Marker
            Marker(
                state = MarkerState(position = otherLocation),
                title = "Friend",
                snippet = "Their location"
            )

            // 📏 Draw a line between both locations
            myLocation?.let {
                Polyline(
                    points = listOf(it, otherLocation),
                    color = Color.Blue,
                    width = 8f
                )
            }
        }

        // 📏 Distance Card (Bottom)
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
                    text = distance,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "between you and your friend",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}