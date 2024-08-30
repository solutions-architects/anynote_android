package com.luckhost.lockscreen_notes.presentation.userLogin


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.useCases.network.GetAuthTokenUseCase
import com.luckhost.domain.useCases.network.SignUpUseCase
import com.luckhost.lockscreen_notes.di.ResourceProvider
import com.luckhost.lockscreen_notes.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class LoginViewModel(
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val resourceProvider: ResourceProvider,
): ViewModel() {
    private var authToken = AuthToken(null, null)

    private val _toastNotification = MutableStateFlow("")
    val toastNotification: StateFlow<String> = _toastNotification.asStateFlow()

    private val _isLoadingState = MutableStateFlow(false)
    val isLoadingState: StateFlow<Boolean> = _isLoadingState.asStateFlow()

    private val _errorTextState = MutableStateFlow("")
    val errorTextState: StateFlow<String> = _errorTextState.asStateFlow()

    private val _loginTextState = MutableStateFlow("")
    private val _passwordTextState = MutableStateFlow("")
    private val _passwordRepeatTextState = MutableStateFlow("")
    val loginTextState: StateFlow<String> = _loginTextState.asStateFlow()
    val passwordTextState: StateFlow<String> = _passwordTextState.asStateFlow()
    val passwordRepeatTextState: StateFlow<String> = _passwordRepeatTextState.asStateFlow()

    fun clearErrorText() {
        _errorTextState.value = ""
    }

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
        _errorTextState.value = ""
        if (_passwordTextState.value == _passwordRepeatTextState.value) {
            _isLoadingState.value = true
            viewModelScope.launch {
                val response = signUpUseCase.execute(
                    loginParams = LoginInformation(
                        username = _loginTextState.value,
                        password = _passwordTextState.value
                    )
                )

                when(response) {
                    is Either.Right -> {
                        _toastNotification.value = resourceProvider.getString(
                            R.string.login_activity_registration_successful)
                        _isLoadingState.value = false
                    }
                    is Either.Left -> {
                        _toastNotification.value = "${response.a}"
                        _isLoadingState.value = false
                        Log.e("LoginVM", "error: ${response.a}")
                    }
                }
            }
        } else {
            _toastNotification.value = resourceProvider.getString(
                R.string.login_activity_passwords_does_not_match)
        }
    }

    fun getToken() {
        _isLoadingState.value = true
        _errorTextState.value = ""
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
                    _toastNotification.value = resourceProvider.getString(
                        R.string.login_activity_registration_successful)
                    _isLoadingState.value = false
                }
                is Either.Left -> {

                    val errorMessage = response.a.toString()

                    if (errorMessage.lowercase(Locale.ROOT)
                        .contains("failed to connect to")) {
                        _errorTextState.value = resourceProvider.getString(
                            R.string.login_activity_failed_connect_message)
                    } else if(errorMessage
                        .lowercase(Locale.ROOT)
                        .contains(
                            "no active account found with the given credentials")) {
                        _errorTextState.value = resourceProvider.getString(
                            R.string.login_activity_no_such_account_message)
                    } else {
                        _errorTextState.value = resourceProvider.getString(
                            R.string.login_activity_unexpected_error_message)
                    }


                    _isLoadingState.value = false
                    Log.e("LoginVM", "error: ${response.a}")
                }
            }
        }
    }
}