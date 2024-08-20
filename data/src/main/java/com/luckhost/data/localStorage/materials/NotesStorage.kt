package com.luckhost.data.localStorage.materials

import com.luckhost.data.localStorage.models.Note
import kotlinx.coroutines.flow.Flow

interface NotesStorage {
    fun saveNote(saveObject: Note)

    suspend fun getNotes(noteHashes: List<Int>): Flow<Note>

    fun deleteNote(noteHash: Int)

    fun changeNote(noteHash: Int, saveObject: Note)
}