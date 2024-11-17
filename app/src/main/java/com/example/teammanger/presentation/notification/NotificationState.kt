package com.example.teammanger.presentation.notification

import com.example.domain.model.Notification

data class NotificationState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val notifications: List<Notification> = listOf(),
    val email: String = ""
)
