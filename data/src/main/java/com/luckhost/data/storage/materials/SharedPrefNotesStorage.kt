package com.luckhost.data.storage.materials

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
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

    override fun getNotes(noteHashes: List<Int>): List<Note> {
        val gson = Gson()
        val result: MutableList<Note> = mutableListOf()
        noteHashes.forEach{
            val noteJson = sharedPreferences.getString(it.toString(),
                null)
            if(noteJson != null) {
                result.add(gson.fromJson(noteJson, Note::class.java))
            }
        }
        return result
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