package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.repository.NetworkServiceInterface

class VerifyEmailUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(token: String): Either<NetworkErrorDescription, SuccessDescription> {
        return netApi.verifyEmail(token)
    }
}
