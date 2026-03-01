package com.luckhost.data.localStorage.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UiThemeStorage(
    context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "ui_theme_prefs"
        private const val KEY_DARK_THEME = "key_dark_theme"
    }

    fun saveTheme(isDark: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_DARK_THEME, isDark)
        }
    }

    fun getTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }
}
