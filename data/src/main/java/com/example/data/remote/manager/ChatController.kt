package com.example.data.remote.manager

import com.example.domain.errors.NetWorkError
import com.example.domain.model.Message
import com.example.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ChatController {
    suspend fun joinChat(
        receiver: String,
        img: String
    ): Result<Unit, NetWorkError>

    suspend fun sendMessage(
        message: String,
    ): Result<Unit, NetWorkError>

    fun observeMessages(): Flow<Message>
    suspend fun disconnect(
        receiver: String,
        lastMessage: String
    )
}