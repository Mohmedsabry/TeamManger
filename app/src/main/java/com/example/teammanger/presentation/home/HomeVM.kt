package com.example.teammanger.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repositories.Repository
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        viewModelScope.launch {
            val email = savedStateHandle["email"] ?: ""
            if (email.isNotEmpty()) {
                state = state.copy(
                    member = email
                )
                state = when (val res = repository.getAllTeamsForMember(email)) {
                    is Result.Failure -> {
                        state.copy(
                            error = res.error.name,
                            teams = res.optional ?: listOf()
                        )
                    }

                    is Result.Success -> {
                        println(res.data)
                        state.copy(
                            error = null,
                            teams = res.data
                        )
                    }
                }
            }
        }
    }
}