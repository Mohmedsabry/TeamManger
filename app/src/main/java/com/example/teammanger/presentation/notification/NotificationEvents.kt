package com.example.teammanger.presentation.notification

sealed interface NotificationEvents {
    data object OnClearNotification : NotificationEvents
    data object OnRefresh : NotificationEvents
}