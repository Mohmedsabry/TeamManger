package com.example.data.mapper

import com.example.data.local.entity.TeamEntity
import com.example.data.remote.dto.TeamDto
import com.example.domain.model.Team

fun Team.toDto(): TeamDto {
    return TeamDto(
        name = name,
        members = members.map { it.toDto() },
        description = description,
        admin = admin,
        timeCreated = timeCreated,
        taskEntities = tasks.map { it.toDto() },
        teamId = id
    )
}

fun Team.toEntity(): TeamEntity =
    TeamEntity(
        name = name,
        description = description,
        admin = admin,
        members = members.map { it.toEntity() },
        tasks = tasks.map { it.toEntity() },
        timeCreated = timeCreated
    )

fun TeamDto.toTeam(): Team {
    return Team(
        id = teamId,
        name = name,
        description = description,
        admin = admin,
        members = members.map { it.toMember() },
        tasks = taskEntities.map { it.toTask() },
        timeCreated = timeCreated
    )
}

fun TeamEntity.toTeam() = Team(
    id = id.toString(),
    name = name,
    description = description,
    admin = admin,
    members = members.map { it.toMember() },
    tasks = tasks.map { it.toTask() },
    timeCreated = timeCreated
)
