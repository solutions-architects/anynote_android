package com.luckhost.data.network.retrofit

import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.dto.AccessToken
import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.AccountParams
import com.luckhost.data.network.dto.CreateNoteRequest
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.network.dto.RefreshToken
import com.luckhost.data.network.dto.VerifyTokenAnswer
import com.luckhost.data.network.dto.VerifyTokenRequest
import com.luckhost.data.network.models.Either
import com.luckhost.data.network.models.NetworkError
import com.luckhost.data.localStorage.models.Note
import kotlinx.coroutines.flow.last

class RetrofitModule: NetworkModule  {
    private val netApi = NetApi.retrofitApi

    override suspend fun getAuthToken(
        loginInformation: LoginRequest): Either<NetworkError, LoginAnswerBody> {
        return execute(
            responseCall = { netApi.login(loginInformation) },
            errorStringToGet = "detail",
        ).last()
    }

    override suspend fun register(
        loginInformation: LoginRequest): Either<NetworkError, AccountAnswerBody> {

        return execute(
            responseCall = { netApi.register(loginInformation) },
            errorStringToGet = "detail",
        ).last()
    }

    override suspend fun refreshAccessToken(
        refreshToken: AccessTokens): Either<NetworkError, AccessToken> {
        refreshToken.refreshToken?.let {

            return execute(
                responseCall = { netApi.refreshAccessToken(RefreshToken(it)) },
                errorStringToGet = "detail",
            ).last()
        }
        return Either.Left(
            NetworkError.Api(
                error = mapOf("detail" to "refresh token is null")
            )
        )
    }

    override suspend fun verifyToken(
        token: VerifyTokenRequest): Either<NetworkError, VerifyTokenAnswer> {

        return execute(
            responseCall = { netApi.verifyToken(
                VerifyTokenRequest(
                    token = token.token
                )
            ) },
            errorStringToGet = "detail",
        ).last()
    }

    override suspend fun getUserAccountParams(
        accessToken: AccessTokens): Either<NetworkError, AccountAnswerBody> {

        return execute(
            responseCall = { netApi.getAccountParams(
                token = "Bearer ${accessToken.accessToken}"
            ) },
            errorStringToGet = "detail",
        ).last()
    }

    override suspend fun changeUserAccountParams(accessToken: AccessTokens,
                                                 userParams: AccountParams) {
        netApi.changeAccountParams(
            token = "Bearer ${accessToken.accessToken}",
            request = userParams,
        )
    }

    override suspend fun getAllNotes(accessToken: AccessTokens):
            Either<NetworkError, List<Note>> {

        return execute(
            responseCall = { netApi.getAllNotes(
                token = "Bearer ${accessToken.accessToken}",
            )
            },
            errorStringToGet = "detail",
        ).last()
    }

    override suspend fun createNewNote(accessToken: AccessTokens, note: Note):
            Either<NetworkError, Note> {

        return execute(
            responseCall = { netApi.createNewNote(
                token = "Bearer ${accessToken.accessToken}",
                note = CreateNoteRequest(
                    content = note.content,
                    noteHash = note.noteHash,
                )
            ) },
            errorStringToGet = "detail",
        ).last()
    }

    override suspend fun changeNoteById(accessToken: AccessTokens, note: Note) {
        note.id?.let {
            netApi.changeNoteById(
                token = "Bearer ${accessToken.accessToken}",
                id = it,
                request = note,
            )
        }
    }
}