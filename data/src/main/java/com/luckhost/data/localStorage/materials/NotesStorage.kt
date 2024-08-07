package com.luckhost.data.localStorage.materials

import com.luckhost.data.localStorage.models.Note

interface NotesStorage {
    fun saveNote(saveObject: Note)

    fun getNotes(noteHashes: List<Int>): List<Note>

    fun deleteNote(noteHash: Int)

    fun changeNote(noteHash: Int, saveObject: Note)
}