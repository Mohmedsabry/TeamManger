package com.example.teammanger.presentation.team_details

import com.example.domain.model.Member
import com.example.domain.model.Task

sealed interface TeamDetailsEvent {
    data class DeleteMember(val member: Member) : TeamDetailsEvent
    data class DeleteTask(val task: Task) : TeamDetailsEvent
    data class OnDoneTask(val task: Task, val statue: Boolean) : TeamDetailsEvent
    data object AddMember : TeamDetailsEvent
    data object AddTask : TeamDetailsEvent
    data object OnAddTask : TeamDetailsEvent
    data object Dismiss : TeamDetailsEvent
    data class OnAddMember(val isChecked: Boolean, val member: Member) : TeamDetailsEvent
    data class OnDescChange(val desc: String) : TeamDetailsEvent
    data class OnDeuChange(val deu: String) : TeamDetailsEvent
    data class OnDateChange(val date: Long) : TeamDetailsEvent
}