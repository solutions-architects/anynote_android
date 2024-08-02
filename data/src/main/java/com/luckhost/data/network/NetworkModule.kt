package com.luckhost.data.network

import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.AccountParams
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.storage.models.Note

interface NetworkModule {
    fun getAuthToken(loginInformation: LoginRequest): LoginAnswerBody
    fun register(loginInformation: LoginRequest)
    fun refreshAccessToken(refreshToken: AccessTokens): AccessTokens
    fun verifyToken(refreshToken: AccessTokens): Boolean

    fun getUserAccountParams(accessToken: AccessTokens): AccountAnswerBody
    fun changeUserAccountParams(accessToken: AccessTokens, userParams: AccountParams)

    fun getAllNotes(accessToken: AccessTokens): Note
    fun getNoteById(accessToken: AccessTokens)
}