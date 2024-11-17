package com.example.teammanger.presentation.home

import com.example.domain.model.Team

data class HomeState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val teams: List<Team> = listOf(),
    val member: String = ""
)
