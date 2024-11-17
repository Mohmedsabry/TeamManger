package com.example.teammanger.presentation.signup

data class SignUpState(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phoneNumber: String = "",
    val age: String = "14",
    val gender: String = "male",
    val error: String? = null,
    val isLoading: Boolean = false,
    val signedUp: Boolean = false,
    val emails: List<String> = listOf()
)
