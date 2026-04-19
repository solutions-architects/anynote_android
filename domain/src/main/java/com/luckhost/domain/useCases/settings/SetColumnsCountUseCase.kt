package com.luckhost.domain.useCases.settings

import com.luckhost.domain.repository.NotesLayoutRepoInterface

class SetColumnsCountUseCase(
    private val repo: NotesLayoutRepoInterface
) {
    operator fun invoke(count: Int) {
        repo.setColumnsCount(count)
    }
}
