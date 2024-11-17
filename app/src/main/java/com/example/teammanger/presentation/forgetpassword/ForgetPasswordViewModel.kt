package com.example.teammanger.presentation.forgetpassword

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
class ForgetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ForgetPasswordState())
    fun onEvent(event: ForgetPasswordEvents) {
        when (event) {
            ForgetPasswordEvents.OnCheckCode -> {
                viewModelScope.launch {
                    if (state.code.isNotBlank()) {
                        state = when (val res = authRepository.checkCode(state.code, state.email)) {
                            is Result.Failure -> {
                                state.copy(
                                    error = res.error.name
                                )
                            }

                            is Result.Success -> {
                                state.copy(
                                    error = null,
                                    checkedCode = true
                                )
                            }
                        }
                    } else {
                        state = state.copy(
                            error = "please type code with 6 codes"
                        )
                    }
                }
            }

            ForgetPasswordEvents.OnSendingEmail -> {
                viewModelScope.launch {
                    state = state.copy(
                        isLoading = true
                    )
                    if (state.email.isNotBlank()) {
                        state = when (authRepository.forgetPassword(state.email)) {
                            is Result.Failure -> {
                                state.copy(
                                    error = "There is no account with this email",
                                    isLoading = false
                                )
                            }

                            is Result.Success -> {
                                state.copy(
                                    sentEmail = true,
                                    error = null,
                                    isLoading = false
                                )
                            }
                        }
                    } else state = state.copy(error = "please type your email")
                }
            }

            is ForgetPasswordEvents.OnTypingCode -> {
                state = state.copy(
                    code = event.code
                )
            }

            is ForgetPasswordEvents.OnTypingEmail -> {
                state = state.copy(
                    email = event.email
                )
            }

            is ForgetPasswordEvents.OnTypingPassword -> {
                state = state.copy(
                    password = event.password
                )
            }

            ForgetPasswordEvents.OnUpdatePassword -> {
                viewModelScope.launch {
                    if (state.password.isNotBlank()) {
                        state = when (val res =
                            authRepository.resetPassword(state.password, state.email)) {
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
                            error = "please type new password"
                        )
                    }
                }
            }
        }
    }
}