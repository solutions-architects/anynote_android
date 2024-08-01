package com.luckhost.domain.repository

import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.UserAccountParams

interface NetworkServiceInterface {
    fun getAuthToken(loginInformation: LoginInformation): AuthToken
    fun register(loginInformation: LoginInformation)
    fun refreshAccessToken(refreshToken: AuthToken): AuthToken
    fun verifyToken(refreshToken: AuthToken): Boolean

    fun getUserAccountParams(accessToken: AuthToken): UserAccountParams
    fun changeUserAccountParams(accessToken: AuthToken, userParams: UserAccountParams)

    fun getAllNotes(accessToken: AuthToken)
    fun getNoteById(accessToken: AuthToken)
}