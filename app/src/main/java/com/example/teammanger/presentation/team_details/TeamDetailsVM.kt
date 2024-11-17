package com.example.teammanger.presentation.team_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.repositories.Repository
import com.example.domain.util.Result
import com.example.domain.util.TaskStatue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailsVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle, private val repository: Repository
) : ViewModel() {
    var state by mutableStateOf(TeamDetailsState())
        private set
    private val _showAddMember = MutableStateFlow(false)
    val showAddMember = _showAddMember.asStateFlow()

    private val _showAddTask = MutableStateFlow(false)
    val showAddTask = _showAddTask.asStateFlow()
    val splitter = ", "
    private val _desc = MutableStateFlow("")
    val desc = _desc.asStateFlow()
    private val _deu = MutableStateFlow("")
    private val deu = _deu.asStateFlow()
    private val _date = MutableStateFlow(0L)
    private val date = _date.asStateFlow()

    init {
        val email = savedStateHandle["email"] ?: ""
        val teamId = savedStateHandle["teamId"] ?: ""
        state = state.copy(email = email)
        viewModelScope.launch {
            state = when (val res = repository.getTeam(teamId)) {
                is Result.Failure -> state.copy(error = res.error.name,
                    team = res.optional ?: state.team,
                    isAdmin = res.optional?.admin == email,
                    done = res.optional?.tasks?.count { it.status == TaskStatue.DONE.name.lowercase() }
                        ?: 0)

                is Result.Success -> state.copy(error = null,
                    team = res.data,
                    isAdmin = res.data.admin == email,
                    done = res.data.tasks.count { it.status == TaskStatue.DONE.name.lowercase() })
            }
            state = when (val res = repository.getAllMember()) {
                is Result.Failure -> {
                    state.copy(
                        error = res.error.name,
                        members = res.optional!!
                            .filter { it.email != state.team.admin }
                    )
                }

                is Result.Success -> state.copy(
                    error = null, members = res.data.filter { it.email != state.team.admin }
                )
            }
        }
    }

    fun onEvent(event: TeamDetailsEvent) {
        when (event) {
            TeamDetailsEvent.AddMember -> viewModelScope.launch {
                _showAddMember.emit(true)
            }

            TeamDetailsEvent.AddTask -> viewModelScope.launch {
                _showAddTask.emit(true)
            }

            is TeamDetailsEvent.DeleteMember -> {
                viewModelScope.launch {
                    when (val result = repository.deleteMemberFromTeam(
                        teamId = state.team.id, memberEmail = event.member.email
                    )) {
                        is Result.Failure -> state = state.copy(
                            error = result.error.name
                        )

                        is Result.Success -> {
                            state = state.copy(team = state.team.copy(
                                members = state.team.members - event.member,
                                tasks = state.team.tasks - event.member.tasks.toSet()
                            ),
                                error = null,
                                done = state.team.tasks.count { it.status == TaskStatue.DONE.name.lowercase() })
                        }
                    }
                }
            }

            is TeamDetailsEvent.DeleteTask -> {
                viewModelScope.launch {
                    state = when (val result = repository.deleteTask(event.task)) {
                        is Result.Failure -> {
                            state.copy(
                                error = result.error.name
                            )
                        }

                        is Result.Success -> state.copy(error = null,
                            team = state.team.copy(tasks = state.team.tasks - event.task),
                            done = state.team.tasks.count { it.status == TaskStatue.DONE.name.lowercase() })
                    }
                }
            }

            is TeamDetailsEvent.OnDoneTask -> {
                viewModelScope.launch {
                    state =
                        when (val res = repository.updateTaskStatue(event.task.due, event.statue)) {
                            is Result.Failure -> {
                                state.copy(
                                    error = res.error.name,
                                )
                            }

                            is Result.Success -> state.copy(
                                error = null,
                                done = if (event.statue) state.done + 1 else state.done - 1,
                            )
                        }
                }
            }

            is TeamDetailsEvent.OnDescChange -> viewModelScope.launch {
                _desc.emit(event.desc)
            }

            is TeamDetailsEvent.OnDateChange -> viewModelScope.launch {
                _date.emit(event.date)
            }

            is TeamDetailsEvent.OnDeuChange -> {
                viewModelScope.launch {
                    _deu.emit(event.deu)
                }
            }

            TeamDetailsEvent.OnAddTask -> {
                viewModelScope.launch {
                    val task = Task(
                        id = "",
                        createdTime = System.currentTimeMillis(),
                        creator = state.team.admin,
                        description = desc.value,
                        due = deu.value.split(splitter)[0],
                        status = if ((date.value / 1000) <= (System.currentTimeMillis() / 1000)) TaskStatue.Expired.name.lowercase() else TaskStatue.IN_PROGRESS.name.lowercase(),
                        deadLine = date.value,
                        teamId = state.team.id
                    )
                    state = when (val res = repository.addTask(task)) {
                        is Result.Failure -> state.copy(
                            error = res.error.name,
                        )

                        is Result.Success -> {
                            state.copy(
                                error = null, team = state.team.copy(
                                    tasks = state.team.tasks + task,
                                )
                            )
                        }
                    }
                    _date.emit(System.currentTimeMillis())
                    _desc.emit("")
                    _showAddTask.emit(false)
                }
            }

            is TeamDetailsEvent.OnAddMember -> {
                viewModelScope.launch {
                    state = when (val res =
                        repository.addMemberToTeam(state.team.id, event.member.email)) {
                        is Result.Failure -> state.copy(
                            error = res.error.name
                        )

                        is Result.Success -> state.copy(
                            error = null, team = state.team.copy(
                                members = state.team.members + event.member
                            ), members = state.members - event.member
                        )
                    }
                    _showAddMember.emit(false)
                }
            }

            TeamDetailsEvent.Dismiss -> {
                viewModelScope.launch {
                    _showAddMember.emit(false)
                }
            }
        }
    }
}