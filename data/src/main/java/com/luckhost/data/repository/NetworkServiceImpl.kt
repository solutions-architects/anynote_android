package com.luckhost.data.repository

import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.dto.AccountParams
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.network.models.Either
import com.luckhost.data.network.models.NetworkError
import com.luckhost.data.network.toDomainAuthTokenEither
import com.luckhost.data.network.toDomainNetworkErrorDescription
import com.luckhost.data.network.toDomainOnlyErrorEither
import com.luckhost.data.network.toDomainUserAccountParamsEither

import com.luckhost.domain.models.Either as DomainEither
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.models.network.VerifyTokenAnswer as DomainVerifyTokenAnswer
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
            DomainEither<NetworkErrorDescription, DomainVerifyTokenAnswer> {
        val response = networkModule.verifyToken(
                token = token,
            )

        // at this point, the api returns an empty body,
        // it was decided to simply handle the empty field error as a successful result
        when(response) {
            is Either.Left -> {
                when(val message = response.a) {
                    is NetworkError.Api -> {
                        if(message.error["detail"].equals("errorBody is empty")) {
                            return DomainEither.Right(DomainVerifyTokenAnswer(isValid = true))
                        }
                        return DomainEither.Right(DomainVerifyTokenAnswer(isValid = false))
                    }

                    is NetworkError.Unexpected ->
                        return DomainEither.Left(response.a.toDomainNetworkErrorDescription())
                }
            }
            // unreachable
            is Either.Right -> return DomainEither.Right(DomainVerifyTokenAnswer(isValid = false))
        }
    }

    override suspend fun getUserAccountParams(accessToken: AuthToken):
            DomainEither<NetworkErrorDescription, UserAccountParams> {
        val response = networkModule.getUserAccountParams(
            accessToken = AccessTokens(
                accessToken = accessToken.accessToken,
                refreshToken = accessToken.refreshToken
            )
        )

        return response.toDomainUserAccountParamsEither()
    }

    override suspend fun changeUserAccountParams(accessToken: AuthToken,
                                                 userParams: UserAccountParams):
            DomainEither<NetworkErrorDescription, SuccessDescription> {
        val response = networkModule.changeUserAccountParams(
                accessToken = AccessTokens(
                    accessToken = accessToken.accessToken,
                    refreshToken = accessToken.refreshToken
                ),
                userParams = AccountParams(
                    username = userParams.username,
                    firstName = userParams.firstName,
                    lastName = userParams.lastName,
                    email = userParams.email,
                    isStuff = userParams.isStuff
                )
            )

        return response.toDomainOnlyErrorEither()
    }

    override suspend fun getAllNotes(accessToken: AuthToken):
            DomainEither<NetworkErrorDescription, List<NoteModel>> {
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