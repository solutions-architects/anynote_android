package com.luckhost.domain.useCases.network.localActions

import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.repository.AuthTokensRepoInterface

class SaveLocalAuthTokenUseCase(
    private val repository: AuthTokensRepoInterface
) {
    fun execute(tokens: AuthToken) {
        repository.saveTokens(tokens = tokens)
    }
}