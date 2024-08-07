package com.luckhost.domain.repository

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.models.network.VerifyTokenAnswer

interface NetworkServiceInterface {
    suspend fun getAuthToken(loginInformation: LoginInformation):
            Either<NetworkErrorDescription, AuthToken>
    suspend fun register(loginInformation: LoginInformation):
            Either<NetworkErrorDescription, SuccessDescription>
    suspend fun refreshAccessToken(refreshToken: AuthToken):
            Either<NetworkErrorDescription, AuthToken>
    suspend fun verifyToken(refreshToken: AuthToken):
            Either<NetworkErrorDescription, VerifyTokenAnswer>

    suspend fun getUserAccountParams(accessToken: AuthToken):
            Either<NetworkErrorDescription, UserAccountParams>
    suspend fun changeUserAccountParams(accessToken: AuthToken, userParams: UserAccountParams):
            Either<NetworkErrorDescription, SuccessDescription>
    suspend fun getAllNotes(accessToken: AuthToken):
            Either<NetworkErrorDescription, SuccessDescription>
    suspend fun createNote(accessToken: AuthToken, note: NoteModel):
            Either<NetworkErrorDescription, NoteModel>
    suspend fun changeNoteById(accessToken: AuthToken, note: NoteModel):
            Either<NetworkErrorDescription, SuccessDescription>
}