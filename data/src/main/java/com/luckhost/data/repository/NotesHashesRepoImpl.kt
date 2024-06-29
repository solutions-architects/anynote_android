package com.luckhost.data.repository

import com.luckhost.data.storage.keys.HashStorage
import com.luckhost.domain.repository.NotesHashesRepoInterface

class NotesHashesRepoImpl(
    private val hashStorage: HashStorage
): NotesHashesRepoInterface {
    override fun saveHashes(noteHashes: List<Int>) {
        hashStorage.saveHashes(noteHashes)
    }

    override fun getHashes(): List<Int> {
        return hashStorage.getHashes()
    }
}