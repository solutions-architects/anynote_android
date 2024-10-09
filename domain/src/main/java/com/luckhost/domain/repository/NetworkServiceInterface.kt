package com.luckhost.domain.repository

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.models.network.VerifyTokenAnswer
import com.luckhost.domain.models.network.VerifyTokenRequest

/**
 * Auth client and api manager
 */
interface NetworkServiceInterface {
    suspend fun getAuthToken(loginInformation: LoginInformation):
            Either<NetworkErrorDescription, AuthToken>
    suspend fun register(loginInformation: LoginInformation):
            Either<NetworkErrorDescription, SuccessDescription>
    suspend fun refreshAccessToken(refreshToken: AuthToken):
            Either<NetworkErrorDescription, AuthToken>
    suspend fun verifyToken(token: VerifyTokenRequest):
            Either<NetworkErrorDescription, VerifyTokenAnswer>

    suspend fun getUserAccountParams(accessToken: AuthToken):
            Either<NetworkErrorDescription, UserAccountParams>
    suspend fun changeUserAccountParams(accessToken: AuthToken, userParams: UserAccountParams):
            Either<NetworkErrorDescription, SuccessDescription>
    suspend fun getAllNotes(accessToken: AuthToken):
            Either<NetworkErrorDescription, List<NoteModel>>
    suspend fun createNote(accessToken: AuthToken, note: NoteModel):
            Either<NetworkErrorDescription, NoteModel>
    suspend fun changeNoteById(accessToken: AuthToken, note: NoteModel):
            Either<NetworkErrorDescription, SuccessDescription>
}