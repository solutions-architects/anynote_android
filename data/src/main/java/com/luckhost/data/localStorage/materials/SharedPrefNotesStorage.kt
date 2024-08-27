package com.luckhost.data.localStorage.materials

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.luckhost.data.localStorage.models.Note
import kotlinx.coroutines.flow.flow

class SharedPrefNotesStorage(context: Context): NotesStorage {
    companion object {
        private const val SHARED_PREFS_NAME = "note_prefs"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
    override fun saveNote(saveObject: Note) {
        val noteJson = Gson().toJson(saveObject)
        sharedPreferences.edit().putString(saveObject.noteHash, noteJson).apply()
    }

    override suspend fun getNotes(noteHashes: List<String>) = flow<Note> {
        noteHashes.forEach{
            val noteJson = sharedPreferences.getString(it,
                null)
            if(noteJson != null) {
                emit(Gson().fromJson(noteJson, Note::class.java))
            }
        }
    }

    override fun deleteNote(noteHash: String) {
        sharedPreferences.edit().remove(noteHash).apply()
    }

    override fun changeNote(noteHash: String, saveObject: Note) {
        val noteJson = Gson().toJson(saveObject)
        sharedPreferences.edit().putString(noteHash, noteJson).apply()
    }
}