package com.pinpoint.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.pinpoint.MainScreen
import com.pinpoint.login.LoginScreen

sealed class AppRoute {
    data object Login : AppRoute()
    data object Main : AppRoute()
}

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<AppRoute>(AppRoute.Login) }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { key ->
            when (key) {
                is AppRoute.Login -> NavEntry(key) {
                    LoginScreen(
                        onNavigateToMain = {
                            backStack.clear()
                            backStack.add(AppRoute.Main)
                        }
                    )
                }
                is AppRoute.Main -> NavEntry(key) {
                    MainScreen()
                }
            }
        }
    )
}
