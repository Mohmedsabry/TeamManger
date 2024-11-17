package com.example.domain.model

data class Member(
    val email: String = "",
    val tasks: List<Task> = listOf(),
    val teams: List<String> = listOf(),
    val name: String = "",
    val image: String = "",
    val gender: String = ""
)
