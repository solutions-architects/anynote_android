package com.luckhost.data.storage.materials

import com.luckhost.data.storage.models.Note

interface NotesStorage {
    fun saveNote(saveObject: Note)

    fun getNotes(noteHashes: List<Int>): List<Note>

    fun deleteNote(noteHash: Int)

    fun changeNote(noteHash: Int, saveObject: Note)
}