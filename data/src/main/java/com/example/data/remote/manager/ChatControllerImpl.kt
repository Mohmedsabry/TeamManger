package com.example.data.remote.manager

import android.content.Context
import com.example.data.local.Dao
import com.example.data.local.entity.ChatHistoryEntity
import com.example.data.mapper.toMessage
import com.example.data.remote.dto.MessageDto
import com.example.domain.errors.NetWorkError
import com.example.domain.model.Message
import com.example.domain.util.Result
import com.example.util.getMemberEmail
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatControllerImpl @Inject constructor(
    private val client: HttpClient,
    private val dao: Dao,
    @ApplicationContext private val context: Context
) : ChatController {
    private var session: WebSocketSession? = null
    override suspend fun joinChat(
        receiver: String, img: String
    ): Result<Unit, NetWorkError> {
        val sender = context.getMemberEmail() ?: ""
        val baseUrl = "ws://10.0.2.2:8080/chat?sender=$sender&receiver=$receiver"
        session = client.webSocketSession {
            url(baseUrl)
        }
        if (session?.isActive != true) return Result.Failure(NetWorkError.UNKnown)
        val chat = dao.getChats(sender)
        if (chat.none { it.receiver == receiver }){
            println(chat.none { it.receiver != receiver })
            dao.addChat(
                ChatHistoryEntity(
                    sender = sender, receiver = receiver, receiverImage = img, lastMessage = ""
                )
            )
        }
        return Result.Success(Unit)
    }

    override suspend fun sendMessage(
        message: String,
    ): Result<Unit, NetWorkError> {
        try {
            session?.send(Frame.Text(message))
            return Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Failure(NetWorkError.UNKnown)
        }
    }

    override fun observeMessages(): Flow<Message> {
        return session?.incoming?.receiveAsFlow()?.filter {
            it is Frame.Text
        }?.map {
            val text = (it as Frame.Text).readText()
            val messageDto = Json.decodeFromString<MessageDto>(text)
            messageDto.toMessage()
        } ?: flowOf()
    }

    override suspend fun disconnect(
        receiver: String, lastMessage: String
    ) {
        val sender = context.getMemberEmail() ?: ""
        dao.updateLastMessage(
            message = lastMessage, sender = sender, receiver = receiver
        )
        session?.close()
    }
}