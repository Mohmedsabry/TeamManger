package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member")
data class MemberEntity(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val name: String,
    val image: String,
    val gender: String,
    val tasks: List<TaskEntity>,
    val teams: List<String>
)
