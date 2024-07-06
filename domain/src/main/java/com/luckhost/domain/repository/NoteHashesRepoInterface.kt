package com.luckhost.domain.repository

interface NoteHashesRepoInterface {
    fun saveHashes(noteHashes: List<Int>)
    fun getHashes(): List<Int>
    fun deleteHash(hashToDelete: Int)
}