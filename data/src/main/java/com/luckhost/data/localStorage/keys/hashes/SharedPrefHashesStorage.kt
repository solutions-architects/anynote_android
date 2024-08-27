package com.luckhost.data.localStorage.keys.hashes

import android.content.Context
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken

class SharedPrefHashesStorage(context: Context): HashStorage {
    companion object {
        const val SHARED_PREFS_NAME = "hash_note_prefs"
    }

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFS_NAME,
        Context.MODE_PRIVATE
    )
    override fun saveHashes(noteHashes: List<String>) {
        val gson = Gson()
        val jsonString = gson.toJson(noteHashes)
        sharedPreferences.edit().putString(SHARED_PREFS_NAME, jsonString).apply()
    }

    override fun getHashes(): List<String> {
        val gson = Gson()
        val jsonString = sharedPreferences.getString(
            SHARED_PREFS_NAME, null)?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    override fun deleteHash(hashToDelete: String) {
        val oldList = getHashes()
        saveHashes(oldList.minus(hashToDelete))
    }

    override fun addHash(hash: String) {
        saveHashes(getHashes().plus(hash))
    }
}