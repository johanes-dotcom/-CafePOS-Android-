package com.s1ti.cafeposmobile.ui.screens.login

import com.s1ti.cafeposmobile.data.model.UserRole

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loggedInUserRole: UserRole? = null,
    val loggedInUserName: String? = null
)
