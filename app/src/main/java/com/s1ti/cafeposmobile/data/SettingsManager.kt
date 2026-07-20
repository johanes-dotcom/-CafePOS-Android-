package com.s1ti.cafeposmobile.data

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cafe_pos_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "key_theme"
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
        private const val KEY_LOGGED_IN_USERNAME = "key_logged_in_username"
        
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
        const val THEME_SYSTEM = "system"
        
        const val LANG_ID = "in"
        const val LANG_EN = "en"
    }

    var theme: String
        get() = prefs.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
        set(value) = prefs.edit().putString(KEY_THEME, value).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, LANG_ID) ?: LANG_ID
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var loggedInUsername: String
        get() = prefs.getString(KEY_LOGGED_IN_USERNAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LOGGED_IN_USERNAME, value).apply()

    fun clearSession() {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .putString(KEY_LOGGED_IN_USERNAME, "")
            .apply()
    }

    fun setLocale(context: Context): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
