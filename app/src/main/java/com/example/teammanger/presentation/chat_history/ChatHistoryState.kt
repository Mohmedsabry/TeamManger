package com.example.teammanger.presentation.chat_history

import com.example.domain.model.ChatHistory
import com.example.domain.model.Member

data class ChatHistoryState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val email: String = "",
    val members: List<Member> = listOf(),
    val filteredList: List<Member> = listOf(),
    val searchQuery: String = "",
    val chatHistory: List<ChatHistory> = listOf()
)
