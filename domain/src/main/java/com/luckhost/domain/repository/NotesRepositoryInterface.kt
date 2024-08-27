package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel

/**
 * Local repository of notes that using hash codes as the primary key
 */
interface NotesRepositoryInterface {
    fun saveNote(saveObject: NoteModel)

    suspend fun getNotes(): List<NoteModel>

    suspend fun getNoteByHash(noteHash: String): NoteModel

    suspend fun deleteNote(noteHash: String)

    suspend fun changeNote(noteHash: String, saveObject: NoteModel)
}