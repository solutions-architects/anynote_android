package com.luckhost.data.storage

import com.luckhost.domain.models.NoteModel

interface NotesStorage {
    fun saveNote(saveParam: Note)

    fun getNote(): Note
}