//package com.example.tutorlog.design
//
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.tutorlog.domain.types.BottomBarTabTypes
//
//data class BottomNavItem(
//    val title: String,
//    val icon: ImageVector,
//    val route: String
//)
//
//@Composable
//fun BottomNavigationBar(
//    selectedTab: Int,
//    onTabSelected: (BottomBarTabTypes) -> Unit
//) {
//    val items = listOf(
//        BottomNavItem("Home", Icons.Default.Home, "home"),
//        BottomNavItem("Students", Icons.Default.Person, "students"),
////        BottomNavItem("Events", Icons.Default.DateRange, "profile"),
////        BottomNavItem("MORE", Icons.Default.MoreVert, "more")
//    )
//    Column {
//        HorizontalDivider(color = LocalColors.Neutral600)
//        NavigationBar(
//            containerColor = LocalColors.BackgroundDefaultDark
//        ) {
//            items.forEachIndexed { index, item ->
//                NavigationBarItem(
//                    icon = {
//                        Icon(
//                            imageVector = item.icon,
//                            contentDescription = item.title
//                        )
//                    },
//                    label = {
//                        Text(text = item.title)
//                    },
//                    selected = selectedTab == index,
//                    onClick = {
//                        when (index) {
//                            0 -> onTabSelected(BottomBarTabTypes.HOME)
//                            1 -> onTabSelected(BottomBarTabTypes.STUDENTS)
//                            2 -> onTabSelected(BottomBarTabTypes.EVENTS)
//                            3 -> onTabSelected(BottomBarTabTypes.MORE)
//                        }
//                    },
//                    colors = NavigationBarItemDefaults.colors(
//                        selectedIconColor = LocalColors.White,
//                        selectedTextColor = LocalColors.White,
//                        unselectedIconColor = LocalColors.White.copy(alpha = 0.6f),
//                        unselectedTextColor = LocalColors.White.copy(alpha = 0.6f),
//                        indicatorColor = LocalColors.White.copy(alpha = 0.2f)
//                    )
//                )
//            }
//        }
//    }
//
//}
//
//@Preview
//@Composable
//private fun PreviewBottomNavigationBar() {
//    BottomNavigationBar(selectedTab = 0, onTabSelected = {})
//}