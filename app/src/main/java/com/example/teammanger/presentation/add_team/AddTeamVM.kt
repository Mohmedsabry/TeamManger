package com.example.teammanger.presentation.add_team

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.model.Team
import com.example.domain.repositories.Repository
import com.example.domain.util.Result
import com.example.domain.util.TaskStatue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTeamVM @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(AddTeamState())
        private set
    private val splitter = ", "
    private val _addedTeam = MutableStateFlow(false)
    val addedTeam: StateFlow<Boolean> = _addedTeam
    private val _teamName = MutableStateFlow("")
    val teamName: StateFlow<String> = _teamName
    private val _teamDesc = MutableStateFlow("")
    val teamDesc: StateFlow<String> = _teamDesc
    private val _taskName = MutableStateFlow("")
    val taskName: StateFlow<String> = _taskName
    private val _due = MutableStateFlow("")
    val due: StateFlow<String> = _due
    private val _deadLine = MutableStateFlow(System.currentTimeMillis())
    private val deadLine = _deadLine.asStateFlow()

    init {
        val email = savedStateHandle["email"] ?: ""
        state = state.copy(
            creator = email
        )
        viewModelScope.launch {
            when (val res = repository.getAllMember()) {
                is Result.Failure -> {
                    state = state.copy(
                        error = res.error.name,
                        members = res.optional?.filter { it.email != email } ?: listOf()
                    )
                }

                is Result.Success -> {
                    println(res.data)
                    state = state.copy(
                        error = null,
                        members = res.data.filter { it.email != email },
                        dues = res.data.filter { it.email != email }
                            .map { it.email + splitter + it.name },
                    )
                    _due.emit(state.members.map { it.email + splitter + it.name }.first())
                }
            }
        }
    }

    fun onEvent(event: AddTeamEvent) {
        when (event) {
            is AddTeamEvent.OnAddMember -> {
                state = when (event.isChecked) {
                    true -> state.copy(
                        selectedMembers = state.selectedMembers + event.member
                    )

                    false -> state.copy(
                        selectedMembers = state.selectedMembers - event.member
                    )
                }
            }

            AddTeamEvent.OnAddTask -> {
                val task = Task(
                    id = "",
                    creator = state.creator,
                    deadLine = deadLine.value,
                    description = taskName.value,
                    due = due.value.split(splitter)[0],
                    status = if (deadLine.value < System.currentTimeMillis()) TaskStatue.Expired.name.lowercase() else TaskStatue.IN_PROGRESS.name.lowercase(),
                    createdTime = System.currentTimeMillis(),
                    teamId = ""
                )
                state = state.copy(
                    tasks = state.tasks + task,
                )
                viewModelScope.launch {
                    _due.emit(state.members.map { it.email + splitter + it.name }.first())
                    _deadLine.emit(System.currentTimeMillis())
                    _taskName.emit("")
                }
            }

            AddTeamEvent.OnAddTeam -> {
                viewModelScope.launch {
                    when (val res = repository.addTeam(
                        Team(
                            name = teamName.value,
                            description = teamDesc.value,
                            admin = state.creator,
                            members = state.selectedMembers,
                            tasks = state.tasks,
                            timeCreated = System.currentTimeMillis()
                        )
                    )) {
                        is Result.Failure -> {
                            state = state.copy(
                                error = res.error.name,
                            )
                        }

                        is Result.Success -> {
                            println("success")
                            _addedTeam.emit(true)
                        }
                    }
                }
            }

            is AddTeamEvent.OnDateChange -> {
                viewModelScope.launch {
                    _deadLine.emit(event.date)
                }
            }

            is AddTeamEvent.OnDeleteTask -> {
                state = state.copy(
                    tasks = state.tasks - event.task
                )
            }

            is AddTeamEvent.OnDescChange -> {
                viewModelScope.launch {
                    _taskName.emit(event.desc)
                }
            }

            is AddTeamEvent.OnDueChange -> {
                viewModelScope.launch {
                    _due.emit(event.deu.split(splitter)[0])
                }
            }

            AddTeamEvent.OnNavigationBack -> {

            }

            is AddTeamEvent.OnShowMembers -> state = state.copy(
                showMembers = event.isShown
            )

            is AddTeamEvent.OnTypingTeamDesc -> viewModelScope.launch {
                _teamDesc.emit(event.desc)
            }

            is AddTeamEvent.OnTypingTeamName -> viewModelScope.launch {
                _teamName.emit(event.name)
            }
        }
    }
}