package com.luckhost.data.network.dto

// Refresh and access tokens for further work with the API.
data class LoginAnswerBody(
    val refresh: String?,
    val access: String?
)

