package com.luckhost.domain.useCases.keys

import com.luckhost.domain.repository.NoteHashesRepoInterface

class AddHashUseCase(
    private val hashesRepository: NoteHashesRepoInterface,
) {
    fun execute(hash: String) {
        hashesRepository.addHash(hash)
    }
}