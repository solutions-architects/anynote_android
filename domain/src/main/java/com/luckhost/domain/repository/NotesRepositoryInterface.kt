package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel
import kotlinx.coroutines.flow.Flow


interface NotesRepositoryInterface {
    fun saveNote(saveObject: NoteModel)

    suspend fun getNotes(noteHashes: List<Int>): Flow<NoteModel>

    fun deleteNote(noteHash: Int)

    fun changeNote(noteHash: Int, saveObject: NoteModel)
}