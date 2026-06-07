package com.luckhost.domain.useCases.settings

import com.luckhost.domain.repository.LanguageRepoInterface

class GetLanguageUseCase(private val repo: LanguageRepoInterface) {
    operator fun invoke(): String = repo.getLanguage()
}
