package com.luckhost.data.repository

import com.luckhost.data.localStorage.keys.tokens.TokensStorage
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NotFoundInRepoError
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.repository.AuthTokensRepoInterface

class AuthTokensRepoImpl(
    private val storage: TokensStorage
): AuthTokensRepoInterface {
    override fun saveTokens(tokens: AuthToken) {
        storage.saveTokens(tokens)
    }

    override fun getTokens(): Either<NotFoundInRepoError, AuthToken> {
        val result = storage.getTokensOrNull()
        result?.let {
            return Either.Right(it)
        }
        return Either.Left(NotFoundInRepoError(
            message = "Local storage returns null object"
        ))
    }
}