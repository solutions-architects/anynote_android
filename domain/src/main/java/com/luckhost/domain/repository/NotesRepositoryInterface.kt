package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel

interface NotesRepositoryInterface {
    fun saveNote(saveObject: NoteModel)

    suspend fun getNotes(noteHashes: List<String>): List<NoteModel>

    fun deleteNote(noteHash: String)

    fun changeNote(noteHash: String, saveObject: NoteModel)
}