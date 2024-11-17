package com.example.teammanger.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repositories.AuthRepository
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvents) {
        when (event) {
            LoginEvents.OnLogin -> {
                viewModelScope.launch {
                    if (state.email.isNotBlank() || state.password.isNotBlank()) {
                        state =
                            when (val res = authRepository.signIn(state.email, state.password)) {
                                is Result.Failure -> {
                                    state.copy(
                                        error = res.error.name
                                    )
                                }

                                is Result.Success -> {
                                    state.copy(
                                        error = null,
                                        user = res.data
                                    )
                                }
                            }
                    } else {
                        state = state.copy(
                            error = "please fill all fields"
                        )
                    }
                }
            }

            is LoginEvents.OnTypingEmail -> {
                state = state.copy(
                    email = event.email
                )
            }

            is LoginEvents.OnTypingPassword -> state = state.copy(
                password = event.password
            )
        }
    }
}