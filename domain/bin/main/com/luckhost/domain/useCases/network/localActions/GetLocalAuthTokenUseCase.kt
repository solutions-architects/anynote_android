package com.luckhost.domain.useCases.network.localActions

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.ErrorDescription
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.repository.AuthTokensRepoInterface

class GetLocalAuthTokenUseCase(
    private val repository: AuthTokensRepoInterface
) {
    fun execute(): Either<ErrorDescription, AuthToken> {
        return repository.getTokens()
    }
}