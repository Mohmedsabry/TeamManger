package com.example.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Team(
    val id: String = "",
    val name: String,
    val description: String,
    val admin: String,
    val members: List<Member>,
    val tasks: List<Task>,
    val timeCreated: Long,
) {
    fun timeCreatedPattern(): String {
        val formatter = SimpleDateFormat("MMM dd,yyyy", Locale.US)
        return formatter.format(Date(timeCreated))
    }
}
