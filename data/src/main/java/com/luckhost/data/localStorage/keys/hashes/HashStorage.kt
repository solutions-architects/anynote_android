package com.luckhost.data.localStorage.keys.hashes

interface HashStorage {
    fun getHashes(): List<String>
    fun saveHashes(noteHashes: List<String>)
    fun deleteHash(hashToDelete: String)
    fun addHash(hash: String)
}