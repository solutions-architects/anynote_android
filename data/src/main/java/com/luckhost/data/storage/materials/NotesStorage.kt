package com.luckhost.data.storage.materials

import com.luckhost.data.storage.models.Note

interface NotesStorage {
    fun saveNote(saveObject: Note)

    fun getNote(noteHash: Int): Note
}