package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NotFoundInRepoError
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.repository.AuthTokensRepoInterface

class GetLocalAuthTokenUseCase(
    private val repository: AuthTokensRepoInterface
) {
    fun execute(): Either<NotFoundInRepoError, AuthToken> {
        return repository.getTokens()
    }
}