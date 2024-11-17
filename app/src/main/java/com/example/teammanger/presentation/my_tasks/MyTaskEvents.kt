package com.example.teammanger.presentation.my_tasks

import com.example.domain.model.Task

sealed interface MyTaskEvents {
    data class OnAccept(val task: Task,val statue:Boolean) : MyTaskEvents
    data object OnRefresh:MyTaskEvents
}