package com.luckhost.domain.useCases.settings

import com.luckhost.domain.repository.LanguageRepoInterface

class SetLanguageUseCase(private val repo: LanguageRepoInterface) {
    operator fun invoke(code: String) = repo.setLanguage(code)
}
