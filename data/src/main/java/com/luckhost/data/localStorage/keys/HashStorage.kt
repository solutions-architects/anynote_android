package com.luckhost.data.localStorage.keys

interface HashStorage {
    fun getHashes(): List<Int>
    fun saveHashes(noteHashes: List<Int>)
    fun deleteHash(hashToDelete: Int)
    fun addHash(hash: Int)
}