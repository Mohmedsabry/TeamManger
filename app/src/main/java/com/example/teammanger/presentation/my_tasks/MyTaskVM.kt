package com.example.teammanger.presentation.my_tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repositories.Repository
import com.example.domain.util.Result
import com.example.domain.util.TaskStatue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTaskVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {
    var state by mutableStateOf(MyTaskState())
        private set

    init {
        val email = savedStateHandle["email"] ?: ""
        state = state.copy(
            email = email
        )
        viewModelScope.launch {
            state = when (val res = repository.getAllTasksForMember(email)) {
                is Result.Failure -> state.copy(
                    error = res.error.name,
                    tasks = res.optional?.filter { it.due == email } ?: listOf(),
                    done = res.optional?.count { it.status == TaskStatue.DONE.name.lowercase() &&it.due == email}
                        ?: 0,
                    expired = res.optional?.count { it.status == TaskStatue.Expired.name.lowercase() &&it.due == email}
                        ?: 0,
                    inProgress = res.optional?.count { it.status == TaskStatue.IN_PROGRESS.name.lowercase() &&it.due == email}
                        ?: 0
                )

                is Result.Success -> state.copy(
                    error = null,
                    tasks = res.data.filter { it.due == email },
                    expired = res.data.count { it.status == TaskStatue.Expired.name.lowercase() &&it.due == email},
                    inProgress = res.data.count { it.status == TaskStatue.IN_PROGRESS.name.lowercase() &&it.due == email},
                    done = res.data.count { it.status == TaskStatue.DONE.name.lowercase() &&it.due == email}
                )
            }
        }
    }

    fun event(event: MyTaskEvents) {
        when (event) {
            is MyTaskEvents.OnAccept -> {
                viewModelScope.launch {
                    state = when (val res = repository.updateTaskStatue(event.task.due,event.statue)) {
                        is Result.Failure -> state.copy(
                            error = res.error.name
                        )

                        is Result.Success -> {
                            state.copy(
                                error = null,
                                done = state.done + 1,
                                inProgress = state.inProgress - 1
                            )
                        }
                    }
                }
            }

            MyTaskEvents.OnRefresh -> {
                viewModelScope.launch {
                    println("re")
                    state = when (val res = repository.getAllTasksForMember(state.email)) {
                        is Result.Failure -> state.copy(
                            error = res.error.name,
                            tasks = res.optional?.filter { it.due==state.email } ?: listOf(),
                            done = res.optional?.count { it.status == TaskStatue.DONE.name.lowercase() &&it.due == state.email}
                                ?: 0,
                            expired = res.optional?.count { it.status == TaskStatue.Expired.name.lowercase() &&it.due == state.email}
                                ?: 0,
                            inProgress = res.optional?.count { it.status == TaskStatue.IN_PROGRESS.name.lowercase() &&it.due == state.email}
                                ?: 0
                        )

                        is Result.Success -> state.copy(
                            error = null,
                            tasks = res.data.filter { it.due==state.email },
                            expired = res.data.count { it.status == TaskStatue.Expired.name.lowercase() &&it.due == state.email},
                            inProgress = res.data.count { it.status == TaskStatue.IN_PROGRESS.name.lowercase() &&it.due == state.email},
                            done = res.data.count { it.status == TaskStatue.DONE.name.lowercase() &&it.due == state.email}
                        )
                    }
                }
            }
        }
    }
}