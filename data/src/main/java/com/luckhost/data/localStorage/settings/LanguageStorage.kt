package com.luckhost.data.localStorage.settings

import android.content.Context
import androidx.core.content.edit

class LanguageStorage(context: Context) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "language_prefs"
        private const val KEY_LANGUAGE = "language_code"
        const val DEFAULT_LANGUAGE = "en"
    }

    fun setLanguage(code: String) {
        prefs.edit { putString(KEY_LANGUAGE, code) }
    }

    fun getLanguage(): String = prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
}
