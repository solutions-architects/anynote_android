package com.luckhost.domain.useCases.settings

import com.luckhost.domain.repository.NotesLayoutRepoInterface

class GetColumnsCountUseCase(
    private val repo: NotesLayoutRepoInterface
) {
    operator fun invoke(): Int = repo.getColumnsCount()
}
