package com.luckhost.data.network.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)
