package com.example.data.mapper

import com.example.data.local.entity.ChatHistoryEntity
import com.example.data.remote.dto.MessageDto
import com.example.domain.model.ChatHistory
import com.example.domain.model.Member
import com.example.domain.model.Message

fun MessageDto.toMessage() = Message(
    user = user.toUser(),
    time = time,
    message = message
)

fun ChatHistoryEntity.toChat(receiver: Member): ChatHistory =
    ChatHistory(
        sender = sender,
        receiver = receiver,
        img = receiverImage,
        lastMessage = lastMessage
    )