package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel

interface NotesRepositoryInterface {
    fun saveNote(saveObject: NoteModel)

    suspend fun getNotes(): List<NoteModel>

    fun getNoteByHash(noteHash: String): NoteModel

    fun deleteNote(noteHash: String)

    fun changeNote(noteHash: String, saveObject: NoteModel)
}