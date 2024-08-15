package com.luckhost.lockscreen_notes.presentation.userLogin


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.useCases.network.GetAuthTokenUseCase
import com.luckhost.domain.useCases.network.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
    private val signUpUseCase: SignUpUseCase,
): ViewModel() {
    private var authToken = AuthToken(null, null)

    private val _toastNotification = MutableStateFlow("")
    val toastNotification: StateFlow<String> = _toastNotification.asStateFlow()

    private val _loginTextState = MutableStateFlow("")
    private val _passwordTextState = MutableStateFlow("")
    private val _passwordRepeatTextState = MutableStateFlow("")
    val loginTextState: StateFlow<String> = _loginTextState.asStateFlow()
    val passwordTextState: StateFlow<String> = _passwordTextState.asStateFlow()
    val passwordRepeatTextState: StateFlow<String> = _passwordRepeatTextState.asStateFlow()

    fun clearToastNotification() {
        _toastNotification.value = ""
    }


    fun updateLoginText(newText: String) {
        _loginTextState.value = newText
    }

    fun updatePasswordText(newText: String) {
        _passwordTextState.value = newText
    }

    fun updatePasswordRepeatText(newText: String) {
        _passwordRepeatTextState.value = newText
    }


    fun signUp() {
        viewModelScope.launch {
            val response = signUpUseCase.execute(
                loginParams = LoginInformation(
                    username = loginTextState.value,
                    password = passwordTextState.value
                )
            )

            when(response) {
                is Either.Right -> {
                    _toastNotification.value = "Registration is successful!"
                }
                is Either.Left -> {
                    _toastNotification.value = "${response.a}"
                    Log.e("LoginVM", "error: ${response.a}")
                }

            }
        }
    }

    fun getToken() {
        viewModelScope.launch {
            val response = getAuthTokenUseCase.execute(
                loginParams = LoginInformation(
                    username = loginTextState.value,
                    password = passwordTextState.value
                )
            )

            when(response) {
                is Either.Right -> {
                    authToken = response.b
                    _toastNotification.value = "Login is successful!"
                }
                is Either.Left -> {
                    _toastNotification.value = "${response.a}"
                    Log.e("LoginVM", "error: ${response.a}")
                }

            }
        }
    }

}