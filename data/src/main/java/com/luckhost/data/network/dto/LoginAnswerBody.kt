package com.luckhost.data.network.dto

import com.google.gson.annotations.SerializedName

// Refresh and access tokens for further work with the API.
data class LoginAnswerBody(
    val refresh: String?,
    val access: String?,
    @SerializedName("user_id")
    val userId: Int
)

