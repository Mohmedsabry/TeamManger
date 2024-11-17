package com.example.teammanger.presentation.signup

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.errors.EmailErrors
import com.example.domain.errors.PasswordErrors
import com.example.domain.errors.PhoneNumberErrors
import com.example.domain.repositories.AuthRepository
import com.example.domain.util.Result
import com.example.domain.validatories.EmailValidator
import com.example.domain.validatories.NumberValidator
import com.example.domain.validatories.PasswordValidator
import com.example.teammanger.presentation.signup.Typing.AGE
import com.example.teammanger.presentation.signup.Typing.CONFIRM_PASSWORD
import com.example.teammanger.presentation.signup.Typing.EMAIL
import com.example.teammanger.presentation.signup.Typing.GENDER
import com.example.teammanger.presentation.signup.Typing.PASSWORD
import com.example.teammanger.presentation.signup.Typing.PHONE_NUMBER
import com.example.teammanger.presentation.signup.Typing.USERNAME
import com.example.teammanger.util.Constants.NO_IMAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uri: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val uri: StateFlow<Uri?> = _uri
    var state by mutableStateOf(SignUpState())
        private set
    private val _emailError = MutableStateFlow(false to EmailErrors.IS_EMPTY)
    val emailErrors: StateFlow<Pair<Boolean, EmailErrors>> = _emailError
    private val _passwordError = MutableStateFlow(false to PasswordErrors.IS_EMPTY)
    val passwordErrors: StateFlow<Pair<Boolean, PasswordErrors>> = _passwordError
    private val _phoneError = MutableStateFlow(false to PhoneNumberErrors.IS_EMPTY)
    val phoneErrors: StateFlow<Pair<Boolean, PhoneNumberErrors>> = _phoneError
    fun onEvent(event: SignUpEvents) {
        when (event) {
            is SignUpEvents.OnChangingPhoto -> {
                viewModelScope.launch {
                    _uri.emit(event.uri)
                }
            }

            SignUpEvents.OnSignUp -> {
                viewModelScope.launch {
                    if (!emailErrors.value.first && !passwordErrors.value.first && !phoneErrors.value.first) {
                        when (val res = authRepository.signUp(
                            name = state.userName,
                            password = state.password,
                            email = state.email,
                            age = state.age.toDouble(),
                            phoneNumber = state.phoneNumber,
                            gender = state.gender,
                            img = uri.value?.toString() ?: NO_IMAGE
                        )) {
                            is Result.Failure -> state = state.copy(
                                error = res.error.name
                            )

                            is Result.Success -> state = state.copy(
                                error = null,
                                signedUp = true
                            )
                        }
                    }
                }
            }

            is SignUpEvents.OnTyping -> {
                state = when (event.typing) {
                    USERNAME -> state.copy(userName = event.text)
                    PASSWORD -> state.copy(password = event.text)
                    GENDER -> state.copy(gender = event.text)
                    CONFIRM_PASSWORD -> state.copy(confirmPassword = event.text)
                    PHONE_NUMBER -> state.copy(phoneNumber = event.text)
                    AGE -> state.copy(age = event.text)
                    EMAIL -> state.copy(email = event.text)
                }
            }
        }
    }

    fun checkEmail() {
        viewModelScope.launch {
            when (val res = EmailValidator.invoke(state.email, state.emails)) {
                is Result.Failure -> {
                    _emailError.emit(true to res.error)
                }

                is Result.Success -> {
                    _emailError.emit(false to EmailErrors.IS_EMPTY)
                }
            }
        }
    }

    fun checkPassword() {
        viewModelScope.launch {
            when (val res = PasswordValidator.invoke(state.password)) {
                is Result.Failure -> _passwordError.emit(true to res.error)
                is Result.Success -> _passwordError.emit(false to PasswordErrors.IS_EMPTY)
            }
        }
    }

    fun checkPhone() {
        viewModelScope.launch {
            when (val res = NumberValidator.invoke(state.phoneNumber)) {
                is Result.Failure -> _phoneError.emit(true to res.error)
                is Result.Success -> _phoneError.emit(false to PhoneNumberErrors.IS_EMPTY)
            }
        }
    }
}