package com.luckhost.domain.useCases.network.localActions

import com.luckhost.domain.repository.AuthTokensRepoInterface

class ClearLocalAuthTokenUseCase(
    private val repository: AuthTokensRepoInterface
) {
    fun execute() {
        repository.clearTokens()
    }
}
