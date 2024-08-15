package com.luckhost.data.network

import com.luckhost.data.network.dto.AccessToken
import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.AccountParams
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.network.dto.VerifyTokenAnswer
import com.luckhost.data.network.models.NetworkError
import com.luckhost.data.network.models.Either
import com.luckhost.data.localStorage.models.Note

interface NetworkModule {
    suspend fun getAuthToken(loginInformation: LoginRequest):
            Either<NetworkError, LoginAnswerBody>
    suspend fun register(loginInformation: LoginRequest):
            Either<NetworkError, AccountAnswerBody>

    suspend fun refreshAccessToken(refreshToken: AccessTokens):
            Either<NetworkError, AccessToken>
    suspend fun verifyToken(token: com.luckhost.domain.models.network.VerifyTokenRequest):
            Either<NetworkError, VerifyTokenAnswer>
    suspend fun getUserAccountParams(accessToken: AccessTokens):
            Either<NetworkError, AccountAnswerBody>
    suspend fun changeUserAccountParams(accessToken: AccessTokens, userParams: AccountParams)

    suspend fun getAllNotes(accessToken: AccessTokens):
            Either<NetworkError, List<Note>>
    suspend fun createNewNote(accessToken: AccessTokens, note: Note):
            Either<NetworkError, Note>
    suspend fun changeNoteById(accessToken: AccessTokens, note: Note)
}