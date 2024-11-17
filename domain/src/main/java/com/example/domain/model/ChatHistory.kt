package com.example.domain.model

data class ChatHistory(
    val sender: String,
    val receiver: Member,
    val img: String,
    val lastMessage: String
)
