package com.luckhost.domain.models.network

data class UserAccountParams(
    var username: String = "not specified",
    var firstName: String = "not specified",
    var lastName: String = "not specified",
    var email: String = "not specified",
    var isStuff: Boolean = false,
)
