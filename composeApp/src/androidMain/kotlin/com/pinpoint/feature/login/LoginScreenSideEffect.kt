package com.pinpoint.feature.login

sealed interface LoginScreenSideEffect {
    data object SignInWithGoogle: LoginScreenSideEffect

    data object NavigateToHomeScreen: LoginScreenSideEffect
}