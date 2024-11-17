package com.example.teammanger.presentation.login

import com.example.domain.model.User

data class LoginState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val user: User? = null
)
