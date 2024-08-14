package com.luckhost.domain.repository

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NotFoundInRepoError
import com.luckhost.domain.models.network.AuthToken

interface AuthTokensRepoInterface {
    fun saveTokens(tokens: AuthToken)
    fun getTokens(): Either<NotFoundInRepoError, AuthToken>
}