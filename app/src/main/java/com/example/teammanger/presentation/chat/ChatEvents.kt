package com.example.teammanger.presentation.chat

sealed interface ChatEvents {
    data class OnTyping(val message: String) : ChatEvents
    data object OnSend : ChatEvents
}