package com.pinpoint.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutorlog.design.LocalColors
import com.pinpoint.login.composable.LoginComposable
import com.pinpoint.util.GoogleSignInUtils
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
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
            }
        }
    }




    Scaffold(
        modifier = modifier
            .background(color = LocalColors.Gray900)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxSize()
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