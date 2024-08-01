package com.luckhost.data.storage.models.network

// Refresh and access tokens for further work with the API.
data class LoginAnswerBody(
    val refresh: String?,
    val access: String?
)

