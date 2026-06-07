package com.luckhost.data.network.dto

import com.google.gson.annotations.SerializedName

data class AccountParams(
    val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String,
)
