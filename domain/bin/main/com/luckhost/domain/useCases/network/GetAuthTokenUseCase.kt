package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.repository.NetworkServiceInterface

class GetAuthTokenUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(
        loginParams: LoginInformation
    ): Either<NetworkErrorDescription, AuthToken> {
        val response = netApi.getAuthToken(
            loginInformation =
            loginParams
        )

        return response
    }
}