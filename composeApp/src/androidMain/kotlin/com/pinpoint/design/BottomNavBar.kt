package com.pinpoint.design

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutorlog.design.LocalColors
import com.pinpoint.R
import com.ramcosta.composedestinations.generated.destinations.GroupsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MapScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomTab(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    val label: String
) {
    Explore(MapScreenDestination, R.drawable.ballooon, "Explore"),
    Groups(GroupsScreenDestination, R.drawable.ballooon, "Groups"),
    Profile(ProfileScreenDestination, R.drawable.ballooon, "Profile")
}

@Composable
fun PinPointBottomBar(
    currentTab: BottomTab,
    navigator: DestinationsNavigator
) {
    Column {
        HorizontalDivider(color = LocalColors.BorderLight, thickness = 0.5.dp)
        NavigationBar(
            containerColor = LocalColors.BackgroundDark,
            tonalElevation = 0.dp
        ) {
            BottomTab.entries.forEach { tab ->
                val selected = tab == currentTab
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (selected) return@NavigationBarItem
                        navigator.navigate(tab.direction) {
                            // Always pop back to Map so the backstack is Map -> current
                            popUpTo(MapScreenDestination) {
                                saveState = true
                                inclusive = false
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(tab.icon),
                            contentDescription = tab.label
                        )
                    },
                    label = {
                        Text(
                            tab.label,
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LocalColors.Primary,
                        selectedTextColor = LocalColors.Primary,
                        unselectedIconColor = LocalColors.TextSecondary,
                        unselectedTextColor = LocalColors.TextSecondary,
                        indicatorColor = LocalColors.PrimaryDim
                    )
                )
            }
        }
    }
}
