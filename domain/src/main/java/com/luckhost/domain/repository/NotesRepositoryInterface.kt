package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel

interface NotesRepositoryInterface {
    fun saveNote(saveObject: NoteModel)

    fun getNote(noteHash: Int): NoteModel
}