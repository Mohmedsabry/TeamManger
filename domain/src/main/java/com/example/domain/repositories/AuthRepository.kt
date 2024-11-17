package com.example.domain.repositories

import com.example.domain.errors.NetWorkError
import com.example.domain.model.User
import com.example.domain.util.Result

interface AuthRepository {
    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        age: Double,
        phoneNumber: String,
        gender: String,
        img: String,
    ): Result<Unit, NetWorkError>

    suspend fun signIn(email: String, password: String): Result<User, NetWorkError>
    suspend fun forgetPassword(email: String): Result<Unit, NetWorkError>
    suspend fun checkCode(code: String, email: String): Result<Unit, NetWorkError>
    suspend fun resetPassword(password: String, email: String): Result<User, NetWorkError>
}