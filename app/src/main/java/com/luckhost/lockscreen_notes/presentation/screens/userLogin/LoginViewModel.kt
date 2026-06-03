package com.luckhost.lockscreen_notes.presentation.screens.userLogin


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.LoginInformation
import com.luckhost.domain.models.network.RegisterInformation
import com.luckhost.domain.useCases.github.ClearGithubConnectionUseCase
import com.luckhost.domain.useCases.github.GetGithubUsernameUseCase
import com.luckhost.domain.useCases.github.SaveGithubUsernameUseCase
import com.luckhost.domain.useCases.network.GetAuthTokenUseCase
import com.luckhost.domain.useCases.network.SignUpUseCase
import com.luckhost.domain.useCases.network.VerifyEmailUseCase
import com.luckhost.domain.useCases.network.localActions.ClearLocalAuthTokenUseCase
import com.luckhost.domain.useCases.network.localActions.GetLocalAuthTokenUseCase
import com.luckhost.lockscreen_notes.di.ResourceProvider
import com.luckhost.lockscreen_notes.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

private const val GITHUB_LOGIN_URL = "http://94.183.233.193/api/login/"

class LoginViewModel(
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val clearLocalAuthTokenUseCase: ClearLocalAuthTokenUseCase,
    private val getLocalAuthTokenUseCase: GetLocalAuthTokenUseCase,
    private val getGithubUsernameUseCase: GetGithubUsernameUseCase,
    private val saveGithubUsernameUseCase: SaveGithubUsernameUseCase,
    private val clearGithubConnectionUseCase: ClearGithubConnectionUseCase,
    private val resourceProvider: ResourceProvider,
): ViewModel() {
    private var authToken = AuthToken(null, null)

    private val _isLoggedIn = MutableStateFlow(checkIsLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _githubUsername = MutableStateFlow(getGithubUsernameUseCase.execute())
    val githubUsername: StateFlow<String?> = _githubUsername.asStateFlow()

    private fun checkIsLoggedIn(): Boolean =
        getLocalAuthTokenUseCase.execute() is Either.Right

    private val _toastNotification = MutableStateFlow("")
    val toastNotification: StateFlow<String> = _toastNotification.asStateFlow()

    private val _isLoadingState = MutableStateFlow(false)
    val isLoadingState: StateFlow<Boolean> = _isLoadingState.asStateFlow()

    private val _errorTextState = MutableStateFlow("")
    val errorTextState: StateFlow<String> = _errorTextState.asStateFlow()

    // In login screen: acts as email. In signup screen: acts as username.
    private val _loginTextState = MutableStateFlow("")
    private val _emailTextState = MutableStateFlow("")
    private val _passwordTextState = MutableStateFlow("")
    private val _passwordRepeatTextState = MutableStateFlow("")
    val loginTextState: StateFlow<String> = _loginTextState.asStateFlow()
    val emailTextState: StateFlow<String> = _emailTextState.asStateFlow()
    val passwordTextState: StateFlow<String> = _passwordTextState.asStateFlow()
    val passwordRepeatTextState: StateFlow<String> = _passwordRepeatTextState.asStateFlow()

    private val _signUpSuccessState = MutableStateFlow(false)
    val signUpSuccessState: StateFlow<Boolean> = _signUpSuccessState.asStateFlow()

    private var pendingVerifyToken: String? = null

    fun logout() {
        clearLocalAuthTokenUseCase.execute()
        _isLoggedIn.value = false
    }

    fun connectGithub(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_LOGIN_URL))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun refreshGithubState() {
        _githubUsername.value = getGithubUsernameUseCase.execute()
    }

    fun disconnectGithub() {
        clearGithubConnectionUseCase.execute()
        _githubUsername.value = null
    }

    fun clearErrorText() {
        _errorTextState.value = ""
    }

    fun clearToastNotification() {
        _toastNotification.value = ""
    }

    fun updateLoginText(newText: String) {
        _loginTextState.value = newText
    }

    fun updateEmailText(newText: String) {
        _emailTextState.value = newText
    }

    fun updatePasswordText(newText: String) {
        _passwordTextState.value = newText
    }

    fun updatePasswordRepeatText(newText: String) {
        _passwordRepeatTextState.value = newText
    }

    fun setPendingVerifyToken(token: String) {
        pendingVerifyToken = token
    }

    fun signUp() {
        clearErrorText()
        _signUpSuccessState.value = false
        if (_passwordTextState.value != _passwordRepeatTextState.value) {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_passwords_does_not_match
            )
        } else if (_passwordTextState.value == "") {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_entered_password_is_empty
            )
        } else if (_loginTextState.value == "") {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_entered_login_is_empty
            )
        } else if (_emailTextState.value == "") {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_entered_email_is_empty
            )
        } else {
            _isLoadingState.value = true
            viewModelScope.launch {
                val response = signUpUseCase.execute(
                    params = RegisterInformation(
                        username = _loginTextState.value,
                        email = _emailTextState.value,
                        password = _passwordTextState.value,
                    )
                )

                when(response) {
                    is Either.Right -> {
                        _signUpSuccessState.value = true
                        _isLoadingState.value = false
                    }
                    is Either.Left -> {
                        _signUpSuccessState.value = false
                        _isLoadingState.value = false
                        val errorMessage = response.a.toString()
                        showErrorMessage(errorMessage)
                    }
                }
            }
        }
    }

    fun getToken() {
        if (_passwordTextState.value == "") {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_entered_password_is_empty
            )
        } else if (_loginTextState.value == "") {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_entered_login_is_empty
            )
        } else {
            _isLoadingState.value = true
            _errorTextState.value = ""

            viewModelScope.launch {
                val response = getAuthTokenUseCase.execute(
                    loginParams = LoginInformation(
                        email = loginTextState.value,
                        password = passwordTextState.value
                    )
                )
                when(response) {
                    is Either.Right -> {
                        authToken = response.b
                        _isLoggedIn.value = true
                        _toastNotification.value = resourceProvider.getString(
                            R.string.login_activity_login_successful)
                        _isLoadingState.value = false
                    }
                    is Either.Left -> {
                        _isLoadingState.value = false
                        val errorMessage = response.a.toString()
                        showErrorMessage(errorMessage)
                    }
                }
            }
        }
    }

    fun startEmailVerification() {
        val token = pendingVerifyToken ?: return
        _isLoadingState.value = true
        viewModelScope.launch {
            val response = verifyEmailUseCase.execute(token)
            pendingVerifyToken = null
            when(response) {
                is Either.Right -> {
                    _toastNotification.value = resourceProvider.getString(
                        R.string.login_activity_email_verified)
                    _isLoadingState.value = false
                }
                is Either.Left -> {
                    _isLoadingState.value = false
                    val errorMessage = response.a.toString()
                    showEmailVerifyError(errorMessage)
                }
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
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
        Log.e("LoginVM", "error: $errorMessage")
    }

    private fun showEmailVerifyError(errorMessage: String) {
        if (errorMessage.lowercase(Locale.ROOT).contains("activation expired")) {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_email_verify_expired)
        } else if (errorMessage.lowercase(Locale.ROOT).contains("invalid token")) {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_email_verify_invalid)
        } else if (errorMessage.lowercase(Locale.ROOT).contains("failed to connect to")) {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_failed_connect_message)
        } else {
            _errorTextState.value = resourceProvider.getString(
                R.string.login_activity_unexpected_error_message)
        }
        Log.e("LoginVM", "email verify error: $errorMessage")
    }
}
