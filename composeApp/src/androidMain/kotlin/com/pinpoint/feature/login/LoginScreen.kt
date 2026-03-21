package com.pinpoint.feature.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.pinpoint.feature.login.composable.LoginComposable
import com.pinpoint.util.GoogleSignInUtils
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MapScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RootGraph>(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {}


    viewModel.collectSideEffect {
        when(it) {
            is LoginScreenSideEffect.SignInWithGoogle -> {
                GoogleSignInUtils.doGoogleSignIn(
                    context = context,
                    scope = scope,
                    launcher = launcher,
                    login = { userInfo ->
                        viewModel.createUser(
                            userInfo = userInfo
                        )
                    },
                    onCancel = {
                        viewModel.onSignInCancel()
                    }
                )
            }
            is LoginScreenSideEffect.NavigateToHomeScreen -> {
                navigator.navigate(MapScreenDestination) {
                    popUpTo(LoginScreenDestination) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }


    Scaffold(
        modifier = Modifier
            .background(color = LocalColors.Gray900)
            .fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { contentPadding ->
        LoginComposable(
            modifier = Modifier
                .padding(contentPadding),
            onGoogleSignInClick = {
                viewModel.onSignIn()
            },
            isLoading = state.isLoading
        )
    }
}