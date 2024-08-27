package com.luckhost.domain.useCases.keys

import com.luckhost.domain.repository.NoteHashesRepoInterface

class GetHashesUseCase(
    private val hashesRepo: NoteHashesRepoInterface
) {
    fun execute(): List<String> {
        return hashesRepo.getHashes()
    }
}