package com.s1ti.cafeposmobile

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.s1ti.cafeposmobile.data.SettingsManager
import com.s1ti.cafeposmobile.navigation.AppNavHost
import com.s1ti.cafeposmobile.ui.theme.CafePOSMobileTheme

class MainActivity : ComponentActivity() {
    private lateinit var settingsManager: SettingsManager

    override fun attachBaseContext(newBase: Context) {
        settingsManager = SettingsManager(newBase)
        super.attachBaseContext(settingsManager.setLocale(newBase))
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            var currentTheme by remember { mutableStateOf(settingsManager.theme) }

            val isDark = when (currentTheme) {
                SettingsManager.THEME_DARK -> true
                SettingsManager.THEME_LIGHT -> false
                else -> isSystemInDarkTheme()
            }

            CafePOSMobileTheme(darkTheme = isDark) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Panggil AppNavHost dan kirimkan pengaturan yang dibutuhkan
                    AppNavHost(
                        windowSizeClass = windowSizeClass,
                        settingsManager = settingsManager,
                        onThemeChanged = { currentTheme = it },
                        onLanguageChanged = { recreate() }
                    )
                }
            }
        }
    }
}