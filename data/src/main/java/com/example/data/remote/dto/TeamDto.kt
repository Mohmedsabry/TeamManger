package com.example.data.remote.dto

data class TeamDto(
    val name: String,
    val description: String,
    val admin: String,
    val members: List<MemberDto> = listOf(),
    val taskEntities: List<TaskDto> = listOf(),
    val timeCreated: Long = System.currentTimeMillis(),
    val teamId: String
)