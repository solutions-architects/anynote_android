package com.luckhost.domain.useCases.keys

import com.luckhost.domain.repository.NoteHashesRepoInterface

class DeleteHashUseCase(
    private val hashesRepository: NoteHashesRepoInterface,
) {
    fun execute(hashCode: String) {
        hashesRepository.deleteHash(hashCode)
    }
}