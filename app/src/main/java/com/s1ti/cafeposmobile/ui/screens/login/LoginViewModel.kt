package com.s1ti.cafeposmobile.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s1ti.cafeposmobile.data.SettingsManager
import com.s1ti.cafeposmobile.data.repository.LoginResult
import com.s1ti.cafeposmobile.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onTogglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun onToggleRememberMe() {
        _uiState.value = _uiState.value.copy(rememberMe = !_uiState.value.rememberMe)
    }

    /**
     * REQ-1.2 s.d REQ-1.5: validasi kredensial lalu arahkan sesuai role,
     * atau tampilkan pesan error jika gagal.
     */
    fun onLoginClick() {
        val current = _uiState.value
        if (current.isLoading) return

        _uiState.value = current.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            when (val result = userRepository.login(current.username, current.password)) {
                is LoginResult.Success -> {
                    // Simpan session jika login berhasil
                    settingsManager.isLoggedIn = true
                    settingsManager.loggedInUsername = result.user.username
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loggedInUserRole = result.user.role,
                        loggedInUserName = result.user.username // Gunakan username untuk konsistensi pencarian di database
                    )
                }
                LoginResult.InvalidCredentials -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Email/No. HP atau password yang Anda masukkan salah."
                    )
                }
                LoginResult.EmptyInput -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Email/No. HP dan password wajib diisi."
                    )
                }
            }
        }
    }

    /** Dipanggil setelah navigasi dilakukan supaya event tidak ter-trigger ulang. */
    fun consumeNavigationEvent() {
        _uiState.value = _uiState.value.copy(loggedInUserRole = null, loggedInUserName = null)
    }
}
