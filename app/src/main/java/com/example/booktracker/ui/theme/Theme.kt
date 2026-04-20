package com.example.booktracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BookTrackerLightScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Sage,
    primaryContainer = TealSoft,
    onPrimaryContainer = Ink,
    secondary = Slate,
    onSecondary = Sage,
    secondaryContainer = TealSoft,
    onSecondaryContainer = Ink,
    tertiary = TealSoft,
    onTertiary = Ink,
    background = TealSoft,
    onBackground = Ink,
    surface = Sage,
    onSurface = Ink,
    surfaceVariant = TealSoft,
    onSurfaceVariant = Accent,
    outline = Ink.copy(alpha = 0.12f),
    outlineVariant = Ink.copy(alpha = 0.08f),
)

@Composable
fun BookTrackerTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = BookTrackerLightScheme,
        typography = Typography,
        content = content,
    )
}
