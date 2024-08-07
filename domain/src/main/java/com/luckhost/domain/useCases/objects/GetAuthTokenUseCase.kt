package com.luckhost.domain.useCases.objects

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.repository.NetworkServiceInterface

class GetAuthTokenUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(
    ): AuthToken {
        val response = netApi.getAuthToken(
            loginInformation =
            LoginInformation("LuckHost", "SuperPuperProg")
        )

        if (response is Either.Right) {
            return response.b
        }

        return AuthToken("", "")
    }
}