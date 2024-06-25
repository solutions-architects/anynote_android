package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel

interface NotesRepositoryInterface {
    fun saveNote(saveParam: NoteModel)

    fun getNote(): NoteModel
}