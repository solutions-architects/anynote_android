package com.luckhost.data.repository

import androidx.appcompat.app.AppCompatDelegate
import com.luckhost.data.localStorage.theme.UiThemeStorage
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
        AppCompatDelegate.setDefaultNightMode(
            if (isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
