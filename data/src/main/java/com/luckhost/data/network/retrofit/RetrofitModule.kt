package com.luckhost.data.network.retrofit

import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.AccountParams
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.storage.models.Note

class RetrofitModule(): NetworkModule  {
    override fun getAuthToken(loginInformation: LoginRequest): LoginAnswerBody {
        TODO("Not yet implemented")
    }

    override fun register(loginInformation: LoginRequest) {
        TODO("Not yet implemented")
    }

    override fun refreshAccessToken(refreshToken: AccessTokens): AccessTokens {
        TODO("Not yet implemented")
    }

    override fun verifyToken(refreshToken: AccessTokens): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUserAccountParams(accessToken: AccessTokens): AccountAnswerBody {
        TODO("Not yet implemented")
    }

    override fun changeUserAccountParams(accessToken: AccessTokens, userParams: AccountParams) {
        TODO("Not yet implemented")
    }

    override fun getAllNotes(accessToken: AccessTokens): Note {
        TODO("Not yet implemented")
    }

    override fun getNoteById(accessToken: AccessTokens) {
        TODO("Not yet implemented")
    }

}