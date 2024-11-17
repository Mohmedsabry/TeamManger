package com.example.data.remote.dto


data class NotificationDto(
    val who: String,
    val message: String,
    val time: Long = System.currentTimeMillis(),
)