package com.luckhost.data.repository

import com.luckhost.data.localStorage.keys.tokens.TokensStorage
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.ErrorDescription
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.repository.AuthTokensRepoInterface

class AuthTokensRepoImpl(
    private val storage: TokensStorage
): AuthTokensRepoInterface {
    override fun saveTokens(tokens: AuthToken) {
        storage.saveTokens(tokens)
    }

    override fun getTokens(): Either<ErrorDescription, AuthToken> {
        return try {
            val result = storage.getTokensOrThrow()
            Either.Right(result)
        } catch (e: NoSuchElementException) {
            Either.Left(ErrorDescription.NotFoundInRepoError(
                message = e.message.toString()
            ))
        } catch (e: Exception) {
            Either.Left(
                ErrorDescription.UnexpectedError(
                    message = e.message.toString()
                )
            )
        }
    }
}