package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.repository.NetworkServiceInterface

class ChangeUserAccountParamsUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(
        accessToken: AuthToken,
        userAccountParams: UserAccountParams
    ): Either<NetworkErrorDescription, SuccessDescription> {
        val response = netApi.changeUserAccountParams(
            accessToken = accessToken,
            userParams = userAccountParams
        )

        return response
    }
}