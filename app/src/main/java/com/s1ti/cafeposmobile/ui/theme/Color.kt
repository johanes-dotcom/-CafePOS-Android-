package com.s1ti.cafeposmobile.ui.theme

import androidx.compose.ui.graphics.Color

// Default Material Colors (Unused but kept for structure, updated to Greenish/Neutral)
val Green80 = Color(0xFFA5D6A7)
val GreenGrey80 = Color(0xFFC8E6C9)
val LightGreen80 = Color(0xFFDCEDC8)

val Green40 = Color(0xFF2E7D32)
val GreenGrey40 = Color(0xFF455A64)
val LightGreen40 = Color(0xFF689F38)

// Cafe POS Primary Identity - Green Theme
val CafePrimary = Color(0xFF2E7D32) // Primary Green
val CafeOnPrimary = Color(0xFFFFFFFF)
val CafeSecondary = Color(0xFF388E3C) // Secondary Green
val CafeTertiary = Color(0xFF1B5E20) // Dark Green
val CafeSuccess = Color(0xFF43A047) // Success Green
val CafeError = Color(0xFFD32F2F)
val CafeWarning = Color(0xFFFBC02D)
val CafeLightGreen = Color(0xFFA5D6A7)

// Dark Theme Colors
val CafeBackgroundDark = Color(0xFF121212) // Surface Dark
val CafeSurfaceDark = Color(0xFF121212)
val CafeCardBackgroundDark = Color(0xFF1E1E1E) // Card Dark
val CafeTextPrimaryDark = Color(0xFFFFFFFF)
val CafeTextSecondaryDark = Color(0xFFB0B0B0)

// Light Theme Colors
val CafeBackgroundLight = Color(0xFFF5F7F2) // Background
val CafeSurfaceLight = Color(0xFFFFFFFF)
val CafeCardBackgroundLight = Color(0xFFFFFFFF) // Card Background
val CafeTextPrimaryLight = Color(0xFF111827)
val CafeTextSecondaryLight = Color(0xFF4B5563)

// Legacy compatibility (will be managed by Theme.kt)
var CafeBackground = CafeBackgroundLight
var CafeSurface = CafeSurfaceLight
var CafeCardBackground = CafeCardBackgroundLight
var CafeTextPrimary = CafeTextPrimaryLight
var CafeTextSecondary = CafeTextSecondaryLight
