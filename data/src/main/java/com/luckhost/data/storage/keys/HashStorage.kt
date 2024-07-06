package com.luckhost.data.storage.keys

interface HashStorage {
    fun getHashes(): List<Int>
    fun saveHashes(noteHashes: List<Int>)
    fun deleteHash(hashToDelete: Int)
}