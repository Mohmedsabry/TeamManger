package com.example.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username: String,
    val password: String,
    val email: String,
    val age: Double,
    val gender: String,
    val phoneNumber: String,
    val image: String,
)