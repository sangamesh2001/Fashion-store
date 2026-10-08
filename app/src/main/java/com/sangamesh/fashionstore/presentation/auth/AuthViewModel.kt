package com.sangamesh.fashionstore.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.RegisterInfo
import com.sangamesh.fashionstore.domain.repository.AuthRepository
import com.sangamesh.fashionstore.domain.usecase.FieldErrors
import com.sangamesh.fashionstore.domain.usecase.RegistrationValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val loading: Boolean = false,
    val errors: FieldErrors = FieldErrors(),
    val serverError: String? = null,
    val success: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        val errors = RegistrationValidator.validateLogin(email, password)
        if (!errors.isValid) {
            _state.value = _state.value.copy(errors = errors, serverError = null)
            return
        }
        _state.value = _state.value.copy(loading = true, errors = FieldErrors(), serverError = null)
        viewModelScope.launch {
            when (val result = authRepository.login(email, password)) {
                is AppResult.Success -> _state.value = _state.value.copy(loading = false, success = true)
                is AppResult.Error -> _state.value = _state.value.copy(loading = false, serverError = result.error.userMessage())
            }
        }
    }

    fun register(fullName: String, email: String, phone: String, password: String, confirmPassword: String) {
        val info = RegisterInfo(fullName, email, phone, password)
        val errors = RegistrationValidator.validateRegistration(info, confirmPassword)
        if (!errors.isValid) {
            _state.value = _state.value.copy(errors = errors, serverError = null)
            return
        }
        _state.value = _state.value.copy(loading = true, errors = FieldErrors(), serverError = null)
        viewModelScope.launch {
            when (val result = authRepository.register(info)) {
                is AppResult.Success -> _state.value = _state.value.copy(loading = false, success = true)
                is AppResult.Error -> _state.value = _state.value.copy(loading = false, serverError = result.error.userMessage())
            }
        }
    }
}
