package com.s1ti.cafeposmobile.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CafePrimary,
    secondary = CafeSecondary,
    tertiary = CafeTertiary,
    onPrimary = CafeOnPrimary,
    onSecondary = CafeOnPrimary,
    onTertiary = CafeOnPrimary,
    
    primaryContainer = Color(0xFF1B5E20), // Dark Green for containers in dark mode
    onPrimaryContainer = Color(0xFFA5D6A7), // Light Green for icons
    
    secondaryContainer = Color(0xFF2E7D32),
    onSecondaryContainer = Color(0xFFFFFFFF),

    background = CafeBackgroundDark,
    surface = CafeSurfaceDark,
    onBackground = CafeTextPrimaryDark,
    onSurface = CafeTextPrimaryDark,
    surfaceVariant = CafeCardBackgroundDark,
    onSurfaceVariant = CafeTextSecondaryDark,
    error = CafeError,
    outline = CafeSecondary,
    outlineVariant = CafeCardBackgroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = CafePrimary,
    secondary = CafeSecondary,
    tertiary = CafeTertiary,
    onPrimary = CafeOnPrimary,
    onSecondary = CafeOnPrimary,
    onTertiary = CafeOnPrimary,

    primaryContainer = Color(0xFFE8F5E9), // Very Light Green for containers
    onPrimaryContainer = CafePrimary,     // Dark Green for icons
    
    secondaryContainer = Color(0xFFC8E6C9),
    onSecondaryContainer = CafePrimary,

    background = CafeBackgroundLight,
    surface = CafeSurfaceLight,
    onBackground = CafeTextPrimaryLight,
    onSurface = CafeTextPrimaryLight,
    surfaceVariant = CafeCardBackgroundLight,
    onSurfaceVariant = CafeTextSecondaryLight,
    error = CafeError,
    outline = CafePrimary,
    outlineVariant = Color(0xFFE0E0E0)
)

@Composable
fun CafePOSMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled dynamic color to strictly follow brand green
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
