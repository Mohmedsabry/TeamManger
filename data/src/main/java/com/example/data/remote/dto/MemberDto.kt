package com.example.data.remote.dto

data class MemberDto(
    val email: String,
    val tasks: List<TaskDto>,
    val teams: List<String>,
    val name: String,
    val image: String,
    val gender: String
)
