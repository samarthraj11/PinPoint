package com.pinpoint.feature.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.LandingScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MapScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RootGraph>(start = true)
@Composable
fun LandingScreen(
    navigator: DestinationsNavigator,
    viewModel: LandingViewModel = hiltViewModel()
) {
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LandingScreenSideEffect.NavigateToLogin -> {
                navigator.navigate(LoginScreenDestination) {
                    popUpTo(LandingScreenDestination) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            is LandingScreenSideEffect.NavigateToMap -> {
                navigator.navigate(MapScreenDestination) {
                    popUpTo(LandingScreenDestination) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LocalColors.BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = LocalColors.Primary
        )
    }
}
