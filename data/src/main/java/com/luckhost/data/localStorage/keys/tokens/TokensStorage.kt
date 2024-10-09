package com.luckhost.data.localStorage.keys.tokens

import com.luckhost.domain.models.network.AuthToken

/**
 * Storage of authentication tokens for api
 */
interface TokensStorage {
    fun saveTokens(tokens: AuthToken)
    fun getTokensOrThrow(): AuthToken
}