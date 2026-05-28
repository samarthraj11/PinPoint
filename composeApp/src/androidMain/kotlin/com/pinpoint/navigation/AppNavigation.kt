package com.pinpoint.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tutorlog.design.LocalColors
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    DestinationsNavHost(
        navController = navController,
        navGraph = NavGraphs.root,
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.BackgroundDark)
            .statusBarsPadding()
    )
}
