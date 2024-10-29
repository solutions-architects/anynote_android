package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.repository.NetworkServiceInterface

class SignUpUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(
        loginParams: LoginInformation
    ): Either<NetworkErrorDescription, SuccessDescription> {
        val response = netApi.register(
            loginInformation =
            loginParams
        )

        return response
    }
}