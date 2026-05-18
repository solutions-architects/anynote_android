package com.luckhost.lockscreen_notes.presentation.screens.userLogin.additional

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination(val route: String) {
    data object Login : Destination("login")
    data object SignUp : Destination("signup")
    data object EmailVerification : Destination("email_verification")
    data object EmailVerifying : Destination("email_verifying")
    data object Loading : Destination("loading/{from}") {
        fun createRoute(from: String) = "loading/$from"
    }
}