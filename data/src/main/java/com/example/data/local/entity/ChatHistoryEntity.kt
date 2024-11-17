package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sender: String,
    val receiver: String,
    val receiverImage: String,
    val lastMessage: String
)
