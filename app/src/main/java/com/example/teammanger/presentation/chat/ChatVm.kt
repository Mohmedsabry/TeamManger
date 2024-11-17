package com.example.teammanger.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.remote.manager.ChatController
import com.example.domain.repositories.Repository
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatVm @Inject constructor(
    private val controller: ChatController,
    private val savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {
    var state by mutableStateOf(ChatState())
        private set

    init {
        val receiver = savedStateHandle["receiver"] ?: ""
        val image = savedStateHandle["image"] ?: ""
        state = state.copy(
            receiver = receiver,
            image = image
        )
    }

    fun onEvent(event: ChatEvents) {
        when (event) {
            ChatEvents.OnSend -> {
                viewModelScope.launch {
                    state = when (val res = controller.sendMessage(state.message)) {
                        is Result.Failure -> state.copy(
                            error = res.error.name
                        )

                        is Result.Success -> state.copy(
                            message = "",
                            error = null
                        )
                    }
                }
            }

            is ChatEvents.OnTyping -> {
                state = state.copy(
                    message = event.message
                )
            }
        }
    }

    fun connect() {
        viewModelScope.launch {
            println("connect")
            when (controller.joinChat(receiver = state.receiver, img = state.image)) {
                is Result.Failure -> {
                    state = state.copy(
                        error = "No Internet"
                    )
                }

                is Result.Success -> {
                    state = state.copy(error = null)
                    println("success")
                    controller.observeMessages().collect {
                        val newList = state.messages.toMutableList()
                        newList.add(0, it)
                        state = state.copy(
                            messages = newList
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            state = when (val res = repository.getChatMessages(state.receiver)) {
                is Result.Failure -> state.copy(
                    error = res.error.name
                )

                is Result.Success -> {
                    state.copy(
                        messages = res.data,
                        error = null
                    )
                }
            }
        }
    }

    fun disConnect() {
        viewModelScope.launch {
            println("disconnect")
            controller.disconnect(
                state.receiver,
                state.messages.first().message
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}