package com.example.data.mapper

import com.example.data.remote.dto.NotificationDto
import com.example.domain.model.Notification

fun NotificationDto.toNotification() = Notification(
    who, message, time
)