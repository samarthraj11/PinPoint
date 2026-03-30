package com.pinpoint.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RootGraph>
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ProfileScreenSideEffect.NavigateToLogin -> {
                navigator.navigate(LoginScreenDestination) {
                    popUpTo(NavGraphs.root) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.BackgroundDark)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(LocalColors.PrimaryLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SR",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.Primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Samarth Raj",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColors.TextPrimary
        )

        Text(
            text = "samarth@example.com",
            fontSize = 14.sp,
            color = LocalColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        ProfileInfoCard(label = "Phone", value = "+91 98765 43210")
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInfoCard(label = "Location", value = "New Delhi, India")
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInfoCard(label = "Status", value = "Available")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.logout() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalColors.Red400
            )
        ) {
            Text(
                text = "Logout",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = LocalColors.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun ProfileInfoCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.SurfaceDark
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderLight)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = LocalColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = LocalColors.TextPrimary
            )
        }
    }
}
