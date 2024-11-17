package com.example.teammanger.presentation.add_team

import com.example.domain.model.Member
import com.example.domain.model.Task

data class AddTeamState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val members: List<Member> = listOf(),
    val selectedMembers: List<Member> = listOf(),
    val tasks: List<Task> = listOf(),
    val dues: List<String> = listOf(),
    val showMembers: Boolean = false,
    val creator: String = ""
)
