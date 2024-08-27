package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.repository.AuthTokensRepoInterface
import com.luckhost.domain.repository.NetworkServiceInterface

class GetAuthTokenUseCase(
    private val netApi: NetworkServiceInterface,
    private val tokenStorage: AuthTokensRepoInterface,
) {
    suspend fun execute(
        loginParams: LoginInformation
    ): Either<NetworkErrorDescription, AuthToken> {
        val response = netApi.getAuthToken(
            loginInformation =
            loginParams
        )

        when(response) {
            is Either.Left<*> -> {
                
            }
            is Either.Right<AuthToken> -> {
                tokenStorage.saveTokens(
                    response.b
                )
            }
        }

        return response
    }
}