package com.example.booktracker.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

@Composable
fun BookTrackerTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = view.context.findActivity()?.window ?: return@SideEffect
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = BookTrackerLightScheme,
        typography = Typography,
        content = content,
    )
}
