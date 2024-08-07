package com.luckhost.data.repository

import com.luckhost.data.localStorage.keys.HashStorage
import com.luckhost.domain.repository.NoteHashesRepoInterface

class NoteHashesRepoImpl(
    private val hashStorage: HashStorage
): NoteHashesRepoInterface {
    override fun saveHashes(noteHashes: List<Int>) {
        hashStorage.saveHashes(noteHashes)
    }

    override fun getHashes(): List<Int> {
        return hashStorage.getHashes()
    }

    override fun deleteHash(hashToDelete: Int) {
        hashStorage.deleteHash(hashToDelete)
    }

    override fun addHash(hash: Int) {
        hashStorage.addHash(hash)
    }
}