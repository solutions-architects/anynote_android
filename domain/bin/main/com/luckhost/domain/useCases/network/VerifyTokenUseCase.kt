package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.VerifyTokenAnswer
import com.luckhost.domain.models.network.VerifyTokenRequest
import com.luckhost.domain.repository.NetworkServiceInterface

class VerifyTokenUseCase(
    private val netApi: NetworkServiceInterface
) {
    suspend fun execute(token: VerifyTokenRequest):
            Either<NetworkErrorDescription, VerifyTokenAnswer> {
        val response = netApi.verifyToken(token = token)

        return response
    }
}