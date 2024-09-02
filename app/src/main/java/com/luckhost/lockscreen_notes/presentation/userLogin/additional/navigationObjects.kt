package com.luckhost.lockscreen_notes.presentation.userLogin.additional

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination(val route: String) {
    data object Login : Destination("login")
    data object SignUp : Destination("signup")
    data object Loading : Destination("loading/{from}") {
        fun createRoute(from: String) = "loading/$from"
    }
}