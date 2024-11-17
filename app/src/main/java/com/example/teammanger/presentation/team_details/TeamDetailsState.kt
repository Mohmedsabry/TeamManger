package com.example.teammanger.presentation.team_details

import com.example.domain.model.Member
import com.example.domain.model.Team

data class TeamDetailsState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val isAdmin: Boolean = false,
    val team: Team = Team(
        name = "",
        description = "",
        admin = "",
        members = listOf(),
        tasks = listOf(),
        timeCreated = 0
    ),
    val email: String = "",
    val done: Int = 0,
    val members: List<Member> = listOf()
)
