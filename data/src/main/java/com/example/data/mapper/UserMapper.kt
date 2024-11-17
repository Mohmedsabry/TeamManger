package com.example.data.mapper

import com.example.data.local.entity.MemberEntity
import com.example.data.remote.dto.UserDto
import com.example.domain.model.User

fun UserDto.toUser(): User {
    return User(
        name = username,
        email = email,
        password = password,
        age = age,
        phoneNumber = phoneNumber,
        gender = gender,
        img = image
    )
}

fun UserDto.toEntity(): MemberEntity {
    return MemberEntity(
        name = username,
        email = email,
        gender = gender,
        tasks = listOf(),
        teams = listOf(),
        image = image
    )
}