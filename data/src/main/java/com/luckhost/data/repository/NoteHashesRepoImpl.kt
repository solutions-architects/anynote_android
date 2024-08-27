package com.luckhost.data.repository

import com.luckhost.data.localStorage.keys.hashes.HashStorage
import com.luckhost.domain.repository.NoteHashesRepoInterface

class NoteHashesRepoImpl(
    private val hashStorage: HashStorage
): NoteHashesRepoInterface {
    override fun saveHashes(noteHashes: List<String>) {
        hashStorage.saveHashes(noteHashes)
    }

    override fun getHashes(): List<String> {
        return hashStorage.getHashes()
    }

    override fun deleteHash(hashToDelete: String) {
        hashStorage.deleteHash(hashToDelete)
    }

    override fun addHash(hash: String) {
        hashStorage.addHash(hash)
    }
}