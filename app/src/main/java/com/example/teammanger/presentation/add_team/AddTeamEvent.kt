package com.example.teammanger.presentation.add_team

import com.example.domain.model.Member
import com.example.domain.model.Task

sealed interface AddTeamEvent {
    data class OnDateChange(val date: Long) : AddTeamEvent
    data class OnDescChange(val desc: String) : AddTeamEvent
    data class OnDueChange(val deu: String) : AddTeamEvent
    data object OnAddTask : AddTeamEvent
    data class OnAddMember(val isChecked: Boolean, val member: Member) : AddTeamEvent
    data class OnTypingTeamName(val name: String) : AddTeamEvent
    data class OnTypingTeamDesc(val desc: String) : AddTeamEvent
    data class OnShowMembers(val isShown: Boolean) : AddTeamEvent
    data class OnDeleteTask(val task: Task) : AddTeamEvent
    data object OnAddTeam : AddTeamEvent
    data object OnNavigationBack : AddTeamEvent
}