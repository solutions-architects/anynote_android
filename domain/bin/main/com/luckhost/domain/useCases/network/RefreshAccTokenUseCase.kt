package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.repository.NetworkServiceInterface

class RefreshAccTokenUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(
        token: AuthToken
    ): Either<NetworkErrorDescription, AuthToken> {
        val response = netApi.refreshAccessToken(
            refreshToken = token
        )

        return response
    }
}