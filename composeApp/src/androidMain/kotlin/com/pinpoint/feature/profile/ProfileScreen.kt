package com.pinpoint.feature.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.AsyncImage
import com.example.tutorlog.design.LocalColors
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pinpoint.design.BottomTab
import com.pinpoint.design.PinPointBottomBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MapScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RootGraph>
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    BackHandler {
        navigator.navigate(MapScreenDestination) {
            popUpTo(MapScreenDestination) { inclusive = false }
            launchSingleTop = true
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ProfileScreenSideEffect.NavigateToLogin -> {
                navigator.navigate(LoginScreenDestination) {
                    popUpTo(NavGraphs.root) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    val user = Firebase.auth.currentUser
    val displayName = user?.displayName ?: "User"
    val photoUrl = user?.photoUrl?.toString()
    val email = user?.email ?: ""

    Scaffold(
        containerColor = LocalColors.BackgroundDark,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            PinPointBottomBar(currentTab = BottomTab.Profile, navigator = navigator)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(LocalColors.BackgroundDark)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LocalColors.Primary
                    )
                }
                Text(
                    text = "Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalColors.TextPrimary
                )
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = LocalColors.Primary
                    )
                }
            }

            // Hero Profile Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar with edit button
                Box {
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .border(4.dp, LocalColors.Primary, CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(LocalColors.PrimaryLight)
                    ) {
                        SubcomposeAsyncImage(
                            model = photoUrl,
                            contentDescription = "Profile Avatar of $displayName",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            loading = { InitialsPlaceholder(displayName) },
                            error = { InitialsPlaceholder(displayName) }
                        )
                    }
                    // Edit button
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .border(4.dp, LocalColors.BackgroundDark, CircleShape)
                            .background(LocalColors.Primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = LocalColors.BackgroundDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = displayName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalColors.TextPrimary
                )

                if (email.isNotEmpty()) {
                    Text(
                        text = email,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = LocalColors.Primary
                    )
                }

                Text(
                    text = "Location Sharing Enthusiast",
                    fontSize = 13.sp,
                    color = LocalColors.TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Stats Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(modifier = Modifier.weight(1f), value = "1,240", label = "KM")
                StatCard(modifier = Modifier.weight(1f), value = "48", label = "TRIPS")
                StatCard(modifier = Modifier.weight(1f), value = "15", label = "BADGES")
            }

            Spacer(modifier = Modifier.height(28.dp))

            // My Bike Section
            SectionHeader(title = "My Bike", actionText = "Manage")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LocalColors.SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderLight)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LocalColors.BackgroundDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\uD83D\uDEB2",
                            fontSize = 36.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Canyon Spectral CF 8",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = LocalColors.TextPrimary
                        )
                        Text(
                            text = "Mountain \u2022 Carbon Fiber",
                            fontSize = 13.sp,
                            color = LocalColors.TextSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                text = "\u2699\uFE0F Tuned 2d ago",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LocalColors.Primary
                            )
                            Text(
                                text = "\uD83D\uDCCF 245km left",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LocalColors.TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Achievements Section
            Text(
                text = "Achievements",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AchievementBadge(emoji = "\u26A1", label = "Speed Demon", unlocked = true)
                AchievementBadge(emoji = "\u26F0\uFE0F", label = "Peak Climber", unlocked = true)
                AchievementBadge(emoji = "\uD83D\uDCC5", label = "Daily Rider", unlocked = true)
                AchievementBadge(emoji = "\uD83C\uDFC6", label = "Century Club", unlocked = false)
                AchievementBadge(emoji = "\uD83D\uDDFA\uFE0F", label = "Pathfinder", unlocked = true)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App Settings Section
            Text(
                text = "App Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.TextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SettingsRow(
                    emoji = "\uD83D\uDD14",
                    label = "Notifications",
                    showToggle = true,
                    onClick = {}
                )
                SettingsRow(
                    emoji = "\uD83D\uDD12",
                    label = "Privacy & Security",
                    onClick = {}
                )
                SettingsRow(
                    emoji = "\u2601\uFE0F",
                    label = "Data Synchronization",
                    onClick = {}
                )
                // Sign Out
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.logout() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LocalColors.SurfaceDark)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "\uD83D\uDEAA", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Sign Out",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = LocalColors.Red400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, value: String, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColors.PrimaryDim),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(LocalColors.BorderMedium)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.Primary
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalColors.TextSecondary,
                letterSpacing = 1.5.sp
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColors.TextPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { }
        ) {
            Text(
                text = actionText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = LocalColors.Primary
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = LocalColors.Primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun AchievementBadge(emoji: String, label: String, unlocked: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(
                    width = 2.dp,
                    color = if (unlocked) LocalColors.Primary else LocalColors.BorderMedium,
                    shape = CircleShape
                )
                .background(
                    if (unlocked) LocalColors.PrimaryLight else LocalColors.PrimaryDim,
                    CircleShape
                )
                .then(if (!unlocked) Modifier else Modifier),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 28.sp,
                modifier = if (!unlocked) Modifier then Modifier else Modifier
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (unlocked) LocalColors.TextPrimary else LocalColors.TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp,
            letterSpacing = (-0.3).sp
        )
    }
}

@Composable
private fun SettingsRow(
    emoji: String,
    label: String,
    showToggle: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LocalColors.SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = LocalColors.TextPrimary
                )
            }
            if (showToggle) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(22.dp)
                        .background(LocalColors.Primary, RoundedCornerShape(11.dp)),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(18.dp)
                            .background(LocalColors.BackgroundLight, CircleShape)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = LocalColors.TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun InitialsPlaceholder(displayName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.PrimaryLight, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayName.take(2).uppercase(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = LocalColors.Primary
        )
    }
}
