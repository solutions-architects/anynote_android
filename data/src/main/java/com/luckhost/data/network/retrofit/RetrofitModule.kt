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

class RetrofitModule: NetworkModule  {
    private val netApi = NetApi.retrofitApi

    override suspend fun getAuthToken(
        loginInformation: LoginRequest): Either<NetworkError, LoginAnswerBody> {
        val response = netApi.login(loginInformation)

        return getBodyOrNetworkError(
            response = response,
            errorStringToGet = "detail"
        )
    }

    override suspend fun register(
        loginInformation: LoginRequest): Either<NetworkError, AccountAnswerBody> {
        val response = netApi.register(loginInformation)

        return getBodyOrNetworkError(
            response = response,
            errorStringToGet = "detail"
        )
    }

    override suspend fun refreshAccessToken(
        refreshToken: AccessTokens): Either<NetworkError, AccessToken> {
        refreshToken.refreshToken?.let {
            val response = netApi.refreshAccessToken(RefreshToken(it))

            return getBodyOrNetworkError(
                response = response,
                errorStringToGet = "detail",
            )
        }
        return Either.Left(
            NetworkError.Api(
                error = mapOf("detail" to "refresh token is null")
            )
        )
    }

    override suspend fun verifyToken(
        refreshToken: AccessTokens): Either<NetworkError, VerifyTokenAnswer> {
        val response = netApi.verifyToken(
            VerifyTokenRequest(
                refreshToken.refreshToken.toString()
            )
        )

        return getBodyOrNetworkError(
            response = response,
            errorStringToGet = "detail"
        )
    }

    override suspend fun getUserAccountParams(
        accessToken: AccessTokens): Either<NetworkError, AccountAnswerBody> {
        val response = netApi.getAccountParams(
            token = "Bearer ${accessToken.accessToken}"
        )

        return getBodyOrNetworkError(
            response = response,
            errorStringToGet = "detail"
        )
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
        val response = netApi.getAllNotes(
            token = "Bearer ${accessToken.accessToken}",
        )

        return getBodyOrNetworkError(
            response = response,
            errorStringToGet = "detail"
        )
    }

    override suspend fun createNewNote(accessToken: AccessTokens, note: Note):
            Either<NetworkError, Note> {
        val response = netApi.createNewNote(
            token = "Bearer ${accessToken.accessToken}",
            note = CreateNoteRequest(
                content = note.content,
                noteHash = note.noteHash,
            )
        )

        return getBodyOrNetworkError(
            response = response,
            errorStringToGet = "detail"
        )
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