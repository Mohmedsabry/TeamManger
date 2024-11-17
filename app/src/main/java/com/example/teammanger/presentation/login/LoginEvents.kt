package com.example.teammanger.presentation.login

sealed interface LoginEvents {
    data object OnLogin : LoginEvents
    data class OnTypingEmail(val email: String) : LoginEvents
    data class OnTypingPassword(val password: String) : LoginEvents
}