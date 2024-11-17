package com.example.data.remote.dto

import com.example.data.remote.util.RequestStatue
import java.util.*

data class RequestDto(
    val id: String,
    val admin: String,
    val time: Long = System.currentTimeMillis(),
    val teamId: String,
    val statues: String = RequestStatue.PENDING.name.lowercase(Locale.getDefault()),
)
