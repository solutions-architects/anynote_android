package com.luckhost.data.network

import com.luckhost.data.network.dto.AccessToken
import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.models.*
import com.luckhost.data.network.models.Either

import com.luckhost.domain.models.Either as DomainEither
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.SuccessDescription
import com.luckhost.domain.models.network.UserAccountParams

fun <L, R, LD, RD> Either<L, R>.toDomainEither(
    leftTransform: (L) -> LD,
    rightTransform: (R) -> RD
): DomainEither<LD, RD> {
    return when (this) {
        is Either.Left -> DomainEither.Left(leftTransform(this.a))
        is Either.Right -> DomainEither.Right(rightTransform(this.b))
    }
}

fun <T> Either<NetworkError, T>.toDomainOnlyErrorEither():
        DomainEither<NetworkErrorDescription, SuccessDescription> {
    return this.toDomainEither(
        leftTransform = { it.toDomainNetworkErrorDescription() },
        rightTransform = { SuccessDescription(message = "") }
    )
}

fun Either<NetworkError, LoginAnswerBody>.toDomainAuthTokenEither():
        DomainEither<NetworkErrorDescription, AuthToken> {
    return this.toDomainEither(
        leftTransform = { it.toDomainNetworkErrorDescription() },
        rightTransform = { it.toDomainAuthToken() }
    )
}

fun Either<NetworkError, AccessToken>.toDomainAuthTokenEither(refreshToken: String):
        DomainEither<NetworkErrorDescription, AuthToken> {
    return this.toDomainEither(
        leftTransform = { it.toDomainNetworkErrorDescription() },
        rightTransform = { it.toDomainAuthToken(refreshToken) }
    )
}


fun Either<NetworkError, AccountAnswerBody>.toDomainUserAccountParamsEither():
        DomainEither<NetworkErrorDescription, UserAccountParams> {
    return this.toDomainEither(
        leftTransform = { it.toDomainNetworkErrorDescription() },
        rightTransform = { it.toDomainUserAccountParams() }
    )
}