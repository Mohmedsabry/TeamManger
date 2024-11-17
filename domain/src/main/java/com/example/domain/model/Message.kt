package com.example.domain.model

data class Message(
    val user: User,
    val time: Long,
    val message: String
)
