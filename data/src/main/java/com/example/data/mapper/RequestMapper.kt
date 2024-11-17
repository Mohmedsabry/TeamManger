package com.example.data.mapper

import com.example.data.remote.dto.RequestDto
import com.example.domain.model.Request

fun RequestDto.toRequest(due: String) = Request(
    id = id,
    admin = admin,
    time = time,
    teamId = teamId,
    statues = statues,
    due = due
)