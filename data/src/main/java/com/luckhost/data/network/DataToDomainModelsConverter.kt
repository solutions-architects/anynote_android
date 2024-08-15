package com.luckhost.data.network

import com.luckhost.data.network.dto.AccessToken
import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.models.NetworkError

import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.UserAccountParams

fun NetworkError.toDomainNetworkErrorDescription(): NetworkErrorDescription {
    return when (this) {
        is NetworkError.Api -> NetworkErrorDescription.Api(this.error.toMutableMap())
        is NetworkError.Unexpected -> NetworkErrorDescription.Unexpected(this.error)
    }
}

fun LoginAnswerBody.toDomainAuthToken(): AuthToken {
    return AuthToken(
        accessToken = this.access,
        refreshToken = this.refresh
    )
}

fun AccessToken.toDomainAuthToken(refreshToken: String): AuthToken {
    return AuthToken(
        accessToken = this.access,
        refreshToken = refreshToken
    )
}

fun AccountAnswerBody.toDomainUserAccountParams(): UserAccountParams {
    val result = UserAccountParams()

    this.username?.let { result.username = it }
    this.first_name?.let { result.firstName = it }
    this.last_name?.let { result.lastName = it }
    this.email?.let { result.email = it }
    this.is_staff?.let { result.isStuff = it }

    return result
}