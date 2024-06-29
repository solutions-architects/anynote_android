package com.luckhost.domain.repository

import com.luckhost.domain.models.NoteModel

interface NotesHashesRepoInterface {
    fun saveHashes(noteHashes: List<Int>)

    fun getHashes(): List<Int>
}