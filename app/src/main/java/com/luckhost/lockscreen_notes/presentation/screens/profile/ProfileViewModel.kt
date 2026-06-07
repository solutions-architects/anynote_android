package com.luckhost.lockscreen_notes.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luckhost.domain.models.Either
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.useCases.network.ChangeUserAccountParamsUseCase
import com.luckhost.domain.useCases.network.GetUserAccountParamsUseCase
import com.luckhost.domain.useCases.network.localActions.ClearLocalAuthTokenUseCase
import com.luckhost.domain.useCases.network.localActions.GetLocalAuthTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserAccountParamsUseCase: GetUserAccountParamsUseCase,
    private val changeUserAccountParamsUseCase: ChangeUserAccountParamsUseCase,
    private val getLocalAuthTokenUseCase: GetLocalAuthTokenUseCase,
    private val clearLocalAuthTokenUseCase: ClearLocalAuthTokenUseCase,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorText = MutableStateFlow("")
    val errorText: StateFlow<String> = _errorText.asStateFlow()

    private val _successText = MutableStateFlow("")
    val successText: StateFlow<String> = _successText.asStateFlow()

    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    // Stub states for future backend endpoints
    private val _changePasswordOld = MutableStateFlow("")
    val changePasswordOld: StateFlow<String> = _changePasswordOld.asStateFlow()

    private val _changePasswordNew = MutableStateFlow("")
    val changePasswordNew: StateFlow<String> = _changePasswordNew.asStateFlow()

    private val _changePasswordConfirm = MutableStateFlow("")
    val changePasswordConfirm: StateFlow<String> = _changePasswordConfirm.asStateFlow()

    init {
        loadProfile()
    }

    private fun getToken(): AuthToken? =
        (getLocalAuthTokenUseCase.execute() as? Either.Right)?.b

    fun loadProfile() {
        val token = getToken() ?: return
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = getUserAccountParamsUseCase.execute(token)) {
                is Either.Right -> {
                    _username.value = result.b.username
                    _firstName.value = result.b.firstName
                    _lastName.value = result.b.lastName
                    _email.value = result.b.email
                }
                is Either.Left -> _errorText.value = "Не удалось загрузить профиль"
            }
            _isLoading.value = false
        }
    }

    fun saveProfile() {
        val token = getToken() ?: return
        if (_username.value.isBlank()) {
            _errorText.value = "Имя пользователя не может быть пустым"
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val params = UserAccountParams(
                username = _username.value,
                firstName = _firstName.value,
                lastName = _lastName.value,
                email = _email.value,
            )
            when (changeUserAccountParamsUseCase.execute(token, params)) {
                is Either.Right -> _successText.value = "Профиль обновлён"
                is Either.Left -> _errorText.value = "Не удалось обновить профиль"
            }
            _isLoading.value = false
        }
    }

    // Stub — requires backend endpoint POST /auth/change-password/
    fun changePassword() {
        if (_changePasswordNew.value != _changePasswordConfirm.value) {
            _errorText.value = "Пароли не совпадают"
            return
        }
        if (_changePasswordNew.value.isBlank()) {
            _errorText.value = "Новый пароль не может быть пустым"
            return
        }
        // TODO: call changePasswordUseCase when backend endpoint is available
        _errorText.value = "Смена пароля через API пока не реализована на сервере"
    }

    // Stub — requires backend endpoint DELETE /profile/
    fun deleteAccount() {
        // TODO: call deleteAccountUseCase when backend endpoint is available
        _errorText.value = "Удаление аккаунта через API пока не реализована на сервере"
    }

    fun logout() {
        clearLocalAuthTokenUseCase.execute()
        _isLoggedOut.value = true
    }

    fun updateUsername(v: String) { _username.value = v }
    fun updateFirstName(v: String) { _firstName.value = v }
    fun updateLastName(v: String) { _lastName.value = v }
    fun updateEmail(v: String) { _email.value = v }
    fun updateChangePasswordOld(v: String) { _changePasswordOld.value = v }
    fun updateChangePasswordNew(v: String) { _changePasswordNew.value = v }
    fun updateChangePasswordConfirm(v: String) { _changePasswordConfirm.value = v }

    fun clearMessages() {
        _errorText.value = ""
        _successText.value = ""
    }
}
