package com.example.data.mapper

import com.example.data.local.entity.TaskEntity
import com.example.data.remote.dto.TaskDto
import com.example.domain.model.Task
import com.example.domain.util.TaskStatue

fun Task.toDto(): TaskDto {
    return TaskDto(
        id = id,
        creator = creator,
        description = description,
        due = due,
        status = if (deadLine <= System.currentTimeMillis()) TaskStatue.Expired.name.lowercase() else status,
        deadLine = deadLine,
        createdTime = createdTime,
        teamId = teamId
    )
}

fun Task.toEntity(): TaskEntity =
    TaskEntity(
        creator = creator,
        description = description,
        due = due,
        status = if (deadLine.div(1000) <= System.currentTimeMillis()
                .div(1000)
        ) TaskStatue.Expired.name.lowercase() else status,
        deadLine = deadLine,
        createdTime = createdTime,
        teamId = teamId
    )

fun TaskDto.toTask(): Task = Task(
    id,
    creator,
    description,
    due,
    if (deadLine <= System.currentTimeMillis()) TaskStatue.Expired.name.lowercase() else status,
    deadLine,
    createdTime,
    teamId
)

fun TaskEntity.toTask(): Task = Task(
    id = id.toString(),
    creator = creator,
    description = description,
    due = due,
    status = if (deadLine <= System.currentTimeMillis()) TaskStatue.Expired.name.lowercase() else status,
    deadLine = deadLine,
    createdTime = createdTime,
    teamId = teamId
)