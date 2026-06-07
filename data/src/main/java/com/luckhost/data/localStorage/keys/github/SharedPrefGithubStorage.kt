package com.luckhost.data.localStorage.keys.github

import android.content.Context

class SharedPrefGithubStorage(context: Context) {
    companion object {
        private const val PREFS_NAME = "github_prefs"
        private const val KEY_USERNAME = "github_username"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? = sharedPreferences.getString(KEY_USERNAME, null)

    fun clear() {
        sharedPreferences.edit().remove(KEY_USERNAME).apply()
    }
}
