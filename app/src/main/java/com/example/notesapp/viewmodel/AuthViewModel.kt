package com.example.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _ui = MutableStateFlow(AuthUiState(isLoggedIn = repo.isLoggedIn()))
    val ui: StateFlow<AuthUiState> = _ui

    fun onEmailChange(v: String) { _ui.value = _ui.value.copy(email = v, error = null) }
    fun onPasswordChange(v: String) { _ui.value = _ui.value.copy(password = v, error = null) }

    fun login() {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, error = null)
            try {
                repo.login(_ui.value.email.trim(), _ui.value.password)
                _ui.value = _ui.value.copy(isLoading = false, isLoggedIn = true)
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(isLoading = false, error = e.message ?: "Login failed")
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, error = null)
            try {
                repo.register(_ui.value.email.trim(), _ui.value.password)
                _ui.value = _ui.value.copy(isLoading = false, isLoggedIn = true)
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(isLoading = false, error = e.message ?: "Register failed")
            }
        }
    }

    fun logout() {
        repo.logout()
        _ui.value = AuthUiState(isLoggedIn = false)
    }
}
