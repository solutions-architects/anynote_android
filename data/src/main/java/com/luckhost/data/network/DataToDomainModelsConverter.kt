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
    return UserAccountParams(
        username = this.username,
        firstName = this.first_name,
        lastName = this.last_name,
        email = this.email,
        isStuff = this.is_staff
    )
}