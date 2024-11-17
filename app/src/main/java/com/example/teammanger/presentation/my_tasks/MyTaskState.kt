package com.example.teammanger.presentation.my_tasks

import com.example.domain.model.Task

data class MyTaskState(
    val error: String? = null,
    val tasks: List<Task> = listOf(),
    val isLoading: Boolean = false,
    val done: Int = 0,
    val expired: Int = 0,
    val inProgress: Int = 0,
    val email: String = ""
)
