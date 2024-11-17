package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("team")
data class TeamEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val admin: String,
    val members: List<MemberEntity>,
    val tasks: List<TaskEntity>,
    val timeCreated: Long,
)
