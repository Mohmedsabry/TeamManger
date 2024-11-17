package com.example.domain.model

data class Request(
    val id: String,
    val admin: String,
    val due: String,
    val time: Long,
    val teamId: String,
    val statues: String,
)
