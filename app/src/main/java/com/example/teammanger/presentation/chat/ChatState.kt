package com.example.teammanger.presentation.chat

import com.example.domain.model.Message

data class ChatState(
    val error: String? = null,
    val message: String = "",
    val isLoading: Boolean = false,
    val messages: List<Message> = listOf(),
    val receiver: String = "",
    val image: String = ""
)
