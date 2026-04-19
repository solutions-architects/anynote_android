package com.luckhost.data.repository

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.luckhost.data.localStorage.settings.UiThemeStorage
import com.luckhost.domain.repository.UiThemeRepoInterface

class UiThemeRepoImpl(
    private val storage: UiThemeStorage
) : UiThemeRepoInterface {

    override fun isDarkTheme(): Boolean {
        return storage.getTheme()
    }

    override fun toggleTheme() {
        val newValue = !storage.getTheme()
        storage.saveTheme(newValue)
        applySystemTheme(newValue)
    }

    private fun applySystemTheme(isDark: Boolean) {
        Log.d("LuckHost", "applySystemTheme ${isDark}")
        AppCompatDelegate.setDefaultNightMode(
            if (isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
