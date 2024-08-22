package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel

interface NotesRepositoryInterface {
    fun saveNote(saveObject: NoteModel)

    suspend fun getNotes(noteHashes: List<Int>): List<NoteModel>

    fun deleteNote(noteHash: Int)

    fun changeNote(noteHash: Int, saveObject: NoteModel)
}