package com.luckhost.data.localStorage.materials

import com.luckhost.data.localStorage.models.Note
import kotlinx.coroutines.flow.Flow

interface NotesStorage {
    fun saveNote(saveObject: Note)

    suspend fun getNotes(noteHashes: List<String>): Flow<Note>

    fun deleteNote(noteHash: String)

    fun changeNote(noteHash: String, saveObject: Note)
}