package com.example.teammanger.presentation.forgetpassword

sealed interface ForgetPasswordEvents {
    data class OnTypingEmail(val email: String) : ForgetPasswordEvents
    data class OnTypingPassword(val password: String) : ForgetPasswordEvents
    data class OnTypingCode(val code: String) : ForgetPasswordEvents
    data object OnCheckCode : ForgetPasswordEvents
    data object OnUpdatePassword : ForgetPasswordEvents
    data object OnSendingEmail : ForgetPasswordEvents
}