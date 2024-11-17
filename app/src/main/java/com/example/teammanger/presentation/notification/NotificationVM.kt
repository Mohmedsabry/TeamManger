package com.example.teammanger.presentation.notification

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
class NotificationVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {
    var state by mutableStateOf(NotificationState())
        private set

    init {
        viewModelScope.launch {
            val email = savedStateHandle["email"] ?: ""
            state = state.copy(email = email)
            async {
                println("1")
                state = when (val res = repository.getNotification(email)) {
                    is Result.Failure -> {
                        state.copy(
                            error = res.error.name,
                        )
                    }

                    is Result.Success -> state.copy(
                        error = null,
                        notifications = res.data
                    )
                }
            }.await()
        }
    }

    fun event(event: NotificationEvents) {
        when (event) {

            NotificationEvents.OnClearNotification -> {
                viewModelScope.launch {
                    state = when (val res = repository.clearNotification(state.email)) {
                        is Result.Failure -> {
                            state.copy(
                                error = res.error.name
                            )
                        }

                        is Result.Success -> {
                            state.copy(
                                error = null,
                                notifications = listOf()
                            )
                        }
                    }
                }
            }

            NotificationEvents.OnRefresh -> {
                viewModelScope.launch {
                    async {
                        println("R1")
                        state = when (val res = repository.getNotification(state.email)) {
                            is Result.Failure -> {
                                state.copy(
                                    error = res.error.name,
                                )
                            }

                            is Result.Success -> state.copy(
                                error = null,
                                notifications = res.data
                            )
                        }
                    }.await()
                }
            }
        }
    }
}