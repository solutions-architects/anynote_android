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
        val gson = Gson()
        val noteJson = gson.toJson(saveObject)
        sharedPreferences.edit().putString(saveObject.noteHash.toString(), noteJson).apply()
    }

    override suspend fun getNotes(noteHashes: List<Int>) = flow<Note> {
        val gson = Gson()
        noteHashes.forEach{
            val noteJson = sharedPreferences.getString(it.toString(),
                null)
            if(noteJson != null) {
                emit(gson.fromJson(noteJson, Note::class.java))
            }
        }
    }

    override fun deleteNote(noteHash: Int) {
        sharedPreferences.edit().remove(noteHash.toString()).apply()
    }

    override fun changeNote(noteHash: Int, saveObject: Note) {
        val gson = Gson()
        val noteJson = gson.toJson(saveObject)
        sharedPreferences.edit().putString(noteHash.toString(), noteJson).apply()
    }
}