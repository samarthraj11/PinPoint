package com.pinpoint

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Location",
            route = "location",
            selectedIcon = R.drawable.ballooon,
            unselectedIcon = R.drawable.ballooon,
        ),
        BottomNavItem(
            label = "Profile",
            route = "profile",
            selectedIcon = R.drawable.ballooon,
            unselectedIcon = R.drawable.ballooon,
        ),
        BottomNavItem(
            label = "Groups",
            route = "groups",
            selectedIcon = R.drawable.ballooon,
            unselectedIcon = R.drawable.ballooon,
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource( R.drawable.ballooon),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "location",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("location") {
                MapScreen()
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("groups") {
                GroupsScreen()
            }
        }
    }
}
