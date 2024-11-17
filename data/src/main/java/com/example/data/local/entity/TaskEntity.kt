package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.remote.util.TaskStatue
import java.util.UUID

@Entity("task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val creator: String,
    val description: String,
    val due: String,
    val status: String = TaskStatue.IN_PROGRESS.toString().lowercase(),
    val deadLine: Long,
    val createdTime: Long,
    val teamId: String
)
