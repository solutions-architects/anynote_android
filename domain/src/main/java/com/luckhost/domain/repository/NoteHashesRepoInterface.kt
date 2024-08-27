package com.luckhost.domain.repository

interface NoteHashesRepoInterface {
    fun saveHashes(noteHashes: List<String>)
    fun getHashes(): List<String>
    fun deleteHash(hashToDelete: String)
    fun addHash(hash: String)
}