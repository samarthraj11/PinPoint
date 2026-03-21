package com.pinpoint.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.example.tutorlog.design.LocalColors
import com.pinpoint.R
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.GroupsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MapScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.isRouteOnBackStackAsState
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator

enum class BottomBarItem(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    val label: String
) {
    Location(MapScreenDestination, R.drawable.ballooon, "Location"),
    Groups(GroupsScreenDestination, R.drawable.ballooon, "Groups"),
    Profile(ProfileScreenDestination, R.drawable.ballooon, "Profile")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentDestination: DestinationSpec? by navController.currentDestinationAsState()

    val shouldShowBottomBar = currentDestination in BottomBarItem.entries.map { it.direction }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(LocalColors.Gray900),
        bottomBar = {
            if (shouldShowBottomBar) {
                val navigator = navController.rememberDestinationsNavigator()
                NavigationBar {
                    BottomBarItem.entries.forEach { item ->
                        val isCurrentDestOnBackStack by navController.isRouteOnBackStackAsState(item.direction)
                        NavigationBarItem(
                            selected = isCurrentDestOnBackStack,
                            onClick = {
                                if (isCurrentDestOnBackStack) {
                                    navigator.popBackStack(item.direction, false)
                                    return@NavigationBarItem
                                }
                                navigator.navigate(item.direction) {
                                    popUpTo(NavGraphs.root) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(item.icon),
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.root,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
