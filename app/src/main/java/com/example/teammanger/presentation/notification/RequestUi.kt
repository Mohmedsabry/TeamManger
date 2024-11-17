package com.example.teammanger.presentation.notification

import com.example.domain.model.Request

data class RequestUi(
    val request: Request,
    val isRevealed: Boolean = false
)
