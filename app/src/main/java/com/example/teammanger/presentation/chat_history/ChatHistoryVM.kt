package com.example.teammanger.presentation.chat_history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repositories.Repository
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatHistoryVM @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ChatHistoryState())
        private set

    init {
        val email = savedStateHandle["email"] ?: ""
        viewModelScope.launch {
            state = async {
                println("1")
                when (val res = repository.getAllMember()) {
                    is Result.Failure -> state.copy(
                        error = res.error.name,
                        email = email,
                        members = res.optional?.filter { it.email != email } ?: listOf(),
                        filteredList = res.optional?.filter { it.email != email } ?: listOf()
                    )

                    is Result.Success -> state.copy(
                        email = email,
                        error = null,
                        members = res.data.filter { it.email != email },
                        filteredList = res.data.filter { it.email != email }
                    )
                }
            }.await()
            async {
                println("2")
                state = when (val result = repository.getAllChatHistory()) {
                    is Result.Failure -> state.copy(
                        error = result.error.name,
                    )

                    is Result.Success -> state.copy(
                        error = null,
                        chatHistory = result.data
                    )
                }
            }.await()
        }
    }

    fun event(event: ChatHistoryEvents) {
        when (event) {
            is ChatHistoryEvents.OnChatClicked -> {}
            ChatHistoryEvents.OnSearchClicked -> {
                state = state.copy(
                    filteredList = state.members.filter {
                        it.email.contains(state.searchQuery) || it.name.contains(
                            state.searchQuery
                        )
                    }
                )
            }

            is ChatHistoryEvents.OnTyping -> {
                if (event.query.isEmpty()) {
                    state = state.copy(
                        filteredList = state.members
                    )
                }
                state = state.copy(
                    searchQuery = event.query,
                    filteredList = state.members.filter {
                        it.email.contains(event.query) || it.name.contains(
                            event.query
                        )
                    }
                )
            }
        }
    }
}