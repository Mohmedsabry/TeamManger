package com.example.domain.model

data class Task(
    val id: String,
    val creator: String,
    val description: String,
    val due: String,
    val status: String,
    val deadLine: Long,
    val createdTime: Long,
    val teamId: String
)
