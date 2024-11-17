package com.example.teammanger.presentation.signup

import android.net.Uri

sealed interface SignUpEvents {
    data class OnTyping(val typing: Typing, val text: String) : SignUpEvents
    data object OnSignUp : SignUpEvents
    data class OnChangingPhoto(val uri: Uri) : SignUpEvents
}