package com.example.data.remote.dto

import com.example.data.remote.util.TaskStatue

data class TaskDto(
    val id: String = "",
    val creator: String,
    val description: String,
    val due: String,
    val status: String = TaskStatue.IN_PROGRESS.toString().lowercase(),
    val deadLine: Long,
    val createdTime: Long = System.currentTimeMillis(),
    val teamId: String
)