package com.luckhost.domain.useCases.theme

import com.luckhost.domain.repository.UiThemeRepoInterface

class GetThemeStateUseCase(
    private val repo: UiThemeRepoInterface
) {
    operator fun invoke(): Boolean {
        return repo.isDarkTheme()
    }
}