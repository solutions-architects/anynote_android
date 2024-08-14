package com.luckhost.data.localStorage.keys.tokens

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luckhost.data.localStorage.keys.hashes.SharedPrefHashesStorage
import com.luckhost.data.localStorage.keys.hashes.SharedPrefHashesStorage.Companion
import com.luckhost.domain.models.network.AuthToken

class SharedPrefTokensStorage(context: Context): TokensStorage {
    companion object {
        private const val SHARED_PREFS_NAME = "network_token_prefs"
    }

    private val sharedPreferences = context.getSharedPreferences(
        SharedPrefHashesStorage.SHARED_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    override fun saveTokens(tokens: AuthToken) {

        val gson = Gson()
        val jsonString = gson.toJson(tokens)

        sharedPreferences.edit()
            .putString(SHARED_PREFS_NAME, jsonString).apply()
    }

    override fun getTokens(): AuthToken? {
        val gson = Gson()
        val jsonString = sharedPreferences.getString(
            SHARED_PREFS_NAME, null)?: return null
        val type = object : TypeToken<AuthToken>() {}.type
        return gson.fromJson(jsonString, type)
    }
}