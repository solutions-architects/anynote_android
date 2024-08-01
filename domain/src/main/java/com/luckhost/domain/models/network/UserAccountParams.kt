package com.luckhost.domain.models.network

data class UserAccountParams(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val isStuff: Boolean,
)
