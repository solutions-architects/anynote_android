package com.luckhost.domain.useCases.theme

import com.luckhost.domain.repository.UiThemeRepoInterface

class ToggleThemeUseCase(
    private val repo: UiThemeRepoInterface
) {
    operator fun invoke() {
        repo.toggleTheme()
    }
}
