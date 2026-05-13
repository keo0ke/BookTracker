package com.example.booktracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.data.remote.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthMode { LOGIN, REGISTER }

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class AuthViewModel : ViewModel() {

    private val repository = ServiceLocator.authRepository

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun switchMode(mode: AuthMode) {
        _state.value = _state.value.copy(mode = mode, errorMessage = null)
    }

    fun submit(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Введите email и пароль")
            return
        }
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = when (_state.value.mode) {
                AuthMode.LOGIN -> repository.login(email.trim(), password)
                AuthMode.REGISTER -> repository.register(email.trim(), password)
            }
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = result.exceptionOrNull()?.let(::humanReadable),
            )
            // При успехе — флоу isLoggedInFlow в MainActivity сам переключит экран.
        }
    }

    private fun humanReadable(t: Throwable): String {
        val msg = t.message ?: t::class.simpleName ?: "Неизвестная ошибка"
        return when {
            msg.contains("Failed to connect", ignoreCase = true) ||
                msg.contains("Unable to resolve host", ignoreCase = true) ||
                msg.contains("timeout", ignoreCase = true) ->
                "Не удалось подключиться к серверу. Проверь, что бэкенд запущен и VPN на ПК отключён."
            else -> msg // сообщение от сервера (из ErrorResponse.message)
        }
    }
}
