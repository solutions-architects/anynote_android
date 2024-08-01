package com.luckhost.domain.models.network

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)
