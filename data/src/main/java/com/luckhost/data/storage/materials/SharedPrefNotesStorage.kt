package com.luckhost.data.storage.materials

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.luckhost.data.storage.models.Note

class SharedPrefNotesStorage(context: Context): NotesStorage {
    companion object {
        private const val SHARED_PREFS_NAME = "note_prefs"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
    override fun saveNote(saveObject: Note) {
        val gson = Gson()
        val noteJson = gson.toJson(saveObject)
        sharedPreferences.edit().putString(saveObject.noteHash.toString(), noteJson).apply()
    }

    override fun getNote(noteHash: Int): Note {
        val gson = Gson()
        val noteJson = sharedPreferences.getString(noteHash.toString(),
            null)
        return gson.fromJson(noteJson, Note::class.java)
    }
}