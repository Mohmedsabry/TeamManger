package com.example.data.mapper

import com.example.data.local.entity.MemberEntity
import com.example.data.remote.dto.MemberDto
import com.example.domain.model.Member

fun Member.toDto(): MemberDto {
    return MemberDto(
        name = name,
        email = email,
        tasks = tasks.map { it.toDto() },
        teams = teams,
        image = image,
        gender = gender
    )
}

fun Member.toEntity(): MemberEntity =
    MemberEntity(
        name = name,
        email = email,
        tasks = tasks.map { it.toEntity() },
        teams = teams,
        image = image,
        gender = gender
    )

fun MemberDto.toMember() = Member(
    email = email,
    tasks = tasks.map { it.toTask() },
    teams = teams,
    name = name,
    image = image,
    gender = gender
)

fun MemberEntity.toMember() = Member(
    email = email,
    tasks = tasks.map { it.toTask() },
    teams = teams,
    name = name,
    image = image,
    gender = gender
)