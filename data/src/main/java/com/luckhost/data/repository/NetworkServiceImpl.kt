package com.luckhost.data.repository

import com.luckhost.data.network.NetworkModule
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.repository.NetworkServiceInterface

class NetworkServiceImpl(
    private val networkModule: NetworkModule,
): NetworkServiceInterface {
    override fun getAuthToken(loginInformation: LoginInformation): AuthToken {
        TODO("Not yet implemented")
    }

    override fun register(loginInformation: LoginInformation) {
        TODO("Not yet implemented")
    }

    override fun refreshAccessToken(refreshToken: AuthToken): AuthToken {
        TODO("Not yet implemented")
    }

    override fun verifyToken(refreshToken: AuthToken): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUserAccountParams(accessToken: AuthToken): UserAccountParams {
        TODO("Not yet implemented")
    }

    override fun changeUserAccountParams(accessToken: AuthToken, userParams: UserAccountParams) {
        TODO("Not yet implemented")
    }

    override fun getAllNotes(accessToken: AuthToken) {
        TODO("Not yet implemented")
    }

    override fun getNoteById(accessToken: AuthToken) {
        TODO("Not yet implemented")
    }
}