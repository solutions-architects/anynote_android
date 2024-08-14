package com.luckhost.data.localStorage.keys.tokens

import com.luckhost.domain.models.network.AuthToken

interface TokensStorage {
    fun saveTokens(tokens: AuthToken)
    fun getTokens(): AuthToken?
}