package com.pinpoint.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.example.tutorlog.design.LocalColors

private val PinPointColorScheme = darkColorScheme(
    primary = LocalColors.Primary,
    onPrimary = LocalColors.White,
    primaryContainer = LocalColors.PrimaryLight,
    onPrimaryContainer = LocalColors.Primary,
    background = LocalColors.BackgroundDark,
    onBackground = LocalColors.TextPrimary,
    surface = LocalColors.SurfaceDark,
    onSurface = LocalColors.TextPrimary,
    surfaceVariant = LocalColors.SurfaceDarkElevated,
    onSurfaceVariant = LocalColors.TextSecondary,
    error = LocalColors.Red400,
    onError = LocalColors.White,
    outline = LocalColors.BorderMedium,
    outlineVariant = LocalColors.BorderLight,
    inverseSurface = LocalColors.BackgroundLight,
    inverseOnSurface = LocalColors.BackgroundDark,
    surfaceContainerHigh = LocalColors.SurfaceDarkElevated,
)

@Composable
fun PinPointTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PinPointColorScheme,
        content = content
    )
}
