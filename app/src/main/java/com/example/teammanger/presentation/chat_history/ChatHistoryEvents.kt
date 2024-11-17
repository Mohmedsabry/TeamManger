package com.example.teammanger.presentation.chat_history

import com.example.domain.model.ChatHistory

sealed interface ChatHistoryEvents {
    data class OnChatClicked(val chatHistory: ChatHistory) : ChatHistoryEvents
    data class OnTyping(val query: String) : ChatHistoryEvents
    data object OnSearchClicked : ChatHistoryEvents
}