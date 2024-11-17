package com.example.teammanger.presentation.forgetpassword

import com.example.domain.model.User

data class ForgetPasswordState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val code: String = "",
    val checkedCode: Boolean = false,
    val sentEmail: Boolean = false,
    val user: User? = null
)
