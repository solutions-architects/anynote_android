package com.luckhost.data.repository

import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.network.toDomainAuthTokenEither
import com.luckhost.data.network.toDomainOnlyErrorEither

import com.luckhost.domain.models.Either as DomainEither
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.models.network.VerifyTokenAnswer
import com.luckhost.domain.models.network.VerifyTokenRequest
import com.luckhost.domain.repository.NetworkServiceInterface

class NetworkServiceImpl(
    private val networkModule: NetworkModule,
): NetworkServiceInterface {
    override suspend fun getAuthToken(loginInformation: LoginInformation):
            DomainEither<NetworkErrorDescription, AuthToken> {
        val response = networkModule.getAuthToken(
            loginInformation = LoginRequest(
                password = loginInformation.password,
                username = loginInformation.username,
        ))

        return response.toDomainAuthTokenEither()
    }

    override suspend fun register(loginInformation: LoginInformation):
            DomainEither<NetworkErrorDescription, SuccessDescription> {
        val response = networkModule.register(
            loginInformation = LoginRequest(
                password = loginInformation.password,
                username = loginInformation.username,
            )
        )

        return response.toDomainOnlyErrorEither()
    }

    override suspend fun refreshAccessToken(refreshToken: AuthToken):
            DomainEither<NetworkErrorDescription, AuthToken> {

        refreshToken.refreshToken?.let {
            val response = networkModule.refreshAccessToken(
                refreshToken = AccessTokens(
                    accessToken = refreshToken.accessToken,
                    refreshToken = it
                )
            )
            return response.toDomainAuthTokenEither(it)
        }

        return DomainEither.Left(NetworkErrorDescription.Api(
            error = mutableMapOf("message" to "refresh token is null"),
        ))
    }

    override suspend fun verifyToken(token: VerifyTokenRequest):
            DomainEither<NetworkErrorDescription, VerifyTokenAnswer> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserAccountParams(accessToken: AuthToken):
            DomainEither<NetworkErrorDescription, UserAccountParams> {
        return DomainEither.Left(NetworkErrorDescription.Api(
            error = mutableMapOf("message" to "\"Not yet implemented\""),
        ))
    }

    override suspend fun changeUserAccountParams(accessToken: AuthToken,
                                                 userParams: UserAccountParams):
            DomainEither<NetworkErrorDescription, SuccessDescription> {
        return DomainEither.Left(NetworkErrorDescription.Api(
            error = mutableMapOf("message" to "\"Not yet implemented\""),
        ))
    }

    override suspend fun getAllNotes(accessToken: AuthToken):
            DomainEither<NetworkErrorDescription, SuccessDescription> {
        return DomainEither.Left(NetworkErrorDescription.Api(
            error = mutableMapOf("message" to "\"Not yet implemented\""),
        ))
    }

    override suspend fun createNote(accessToken: AuthToken, note: NoteModel):
            DomainEither<NetworkErrorDescription, NoteModel> {
        return DomainEither.Left(NetworkErrorDescription.Api(
            error = mutableMapOf("message" to "\"Not yet implemented\""),
        ))
    }

    override suspend fun changeNoteById(accessToken: AuthToken, note: NoteModel):
            DomainEither<NetworkErrorDescription, SuccessDescription> {
        return DomainEither.Left(NetworkErrorDescription.Api(
            error = mutableMapOf("message" to "\"Not yet implemented\""),
        ))
    }

}