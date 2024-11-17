package com.example.teammanger.presentation.signup

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.errors.EmailErrors
import com.example.domain.errors.PasswordErrors
import com.example.domain.errors.PhoneNumberErrors
import com.example.teammanger.R
import com.example.teammanger.presentation.component.DropDownGender
import com.example.teammanger.ui.theme.TeamMangerTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    state: SignUpState,
    imageUri: Uri? = null,
    isEmailError: Pair<Boolean, EmailErrors>,
    isPasswordError: Pair<Boolean, PasswordErrors>,
    isPhoneError: Pair<Boolean, PhoneNumberErrors>,
    onEvent: (SignUpEvents) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        onEvent(SignUpEvents.OnChangingPhoto(uri!!))
    }
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    var showPasswordIcon by remember {
        mutableStateOf(false)
    }
    var showConfirmPasswordIcon by remember {
        mutableStateOf(false)
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.sdp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                Modifier
                    .size(150.sdp)
                    .padding(5.sdp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.sdp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .clickable {
                            launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    contentScale = ContentScale.Crop,
                    fallback = painterResource(R.drawable.avatar_boy),
                )
                Icon(
                    painter = painterResource(R.drawable.edit),
                    null,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(end = 15.dp, bottom = 20.dp)
                        .align(Alignment.BottomEnd)
                        .size(30.sdp)
                        .background(Color.White, CircleShape)
                )
            }
            OutlinedTextField(
                state.userName,
                {
                    onEvent(SignUpEvents.OnTyping(Typing.USERNAME, it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .padding(5.sdp),
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.user), null
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
            )
            AnimatedVisibility(visible = state.userName.isBlank()) {
                Text(
                    "Please Fill UserName",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontSize = 14.ssp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(SignUpEvents.OnTyping(Typing.EMAIL, it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                label = { Text("Email") },
                leadingIcon = { Icon(painter = painterResource(R.drawable.usertag), null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = isEmailError.first,
                singleLine = true,
            )
            AnimatedVisibility(visible = isEmailError.first) {
                when (isEmailError.second) {
                    EmailErrors.USER_IS_EXIST -> {
                        Text(
                            "User with that email is exist",
                            color = Color.Red,
                            fontSize = 14.ssp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    EmailErrors.NOT_MATCH_PATTERN -> {
                        Text(
                            "email must ends with @gmail.com",
                            color = Color.Red,
                            fontSize = 14.ssp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    EmailErrors.IS_EMPTY -> Text(
                        "Email must not be empty",
                        color = Color.Red,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(SignUpEvents.OnTyping(Typing.PASSWORD, it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                label = { Text("Password") },
                trailingIcon = {
                    Icon(
                        painter = if (!showPasswordIcon) painterResource(R.drawable.bi_eye) else painterResource(
                            R.drawable.eyeslash
                        ),
                        null,
                        modifier = Modifier
                            .clickable { showPasswordIcon = !showPasswordIcon }
                            .size(
                                Icons.Default.Email.defaultWidth,
                                Icons.Default.Email.defaultHeight
                            )
                    )
                },
                visualTransformation = if (showPasswordIcon) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = isPasswordError.first,
                singleLine = true,
            )
            AnimatedVisibility(visible = isPasswordError.first) {
                when (isPasswordError.second) {
                    PasswordErrors.IS_EMPTY -> Text(
                        "Email must not be empty",
                        color = Color.Red,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    PasswordErrors.NOT_HAVE_DIGIT -> Text(
                        "password must have at least one digit",
                        color = Color.Red,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    PasswordErrors.NOT_HAVE_UPPERCASE -> Text(
                        "password must have at least uppercase letter",
                        color = Color.Red,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    PasswordErrors.LENGTH -> Text(
                        "min length is 8 letters",
                        color = Color.Red,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { onEvent(SignUpEvents.OnTyping(Typing.CONFIRM_PASSWORD, it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                label = { Text("Confirm Password") },
                trailingIcon = {
                    Icon(
                        painter = if (!showConfirmPasswordIcon) painterResource(R.drawable.bi_eye) else painterResource(
                            R.drawable.eyeslash
                        ),
                        null,
                        modifier = Modifier
                            .clickable { showConfirmPasswordIcon = !showConfirmPasswordIcon }
                            .size(
                                Icons.Default.Email.defaultWidth,
                                Icons.Default.Email.defaultHeight
                            )
                    )
                },
                visualTransformation = if (showConfirmPasswordIcon) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = (state.confirmPassword.isBlank()) || state.confirmPassword != state.password,
                singleLine = true,
            )
            AnimatedVisibility(visible = (state.confirmPassword.isBlank()) || state.confirmPassword != state.password) {
                Text(
                    "password and confirmed must be equal and not empty",
                    color = Color.Red,
                    fontSize = 14.ssp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { onEvent(SignUpEvents.OnTyping(Typing.PHONE_NUMBER, it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                label = { Text("Phone number") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Call,
                        null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = isPhoneError.first,
                singleLine = true,
            )
            AnimatedVisibility(visible = isPhoneError.first) {
                when (isPhoneError.second) {
                    PhoneNumberErrors.IS_EMPTY -> Text(
                        "type phone number",
                        color = Color.Red,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    PhoneNumberErrors.NOT_MATCH_PATTERN -> Text(
                        "phone must be 11 digit with 012 or 011 or 010 or 015 start",
                        color = Color.Red,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            OutlinedTextField(
                value = state.age,
                onValueChange = { onEvent(SignUpEvents.OnTyping(Typing.AGE, it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                label = { Text("Age") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                isError = state.age.isBlank() || state.age.toDouble() < 14,
                singleLine = true,
            )
            AnimatedVisibility(visible = state.age.isBlank() || state.age.toDouble() < 14) {
                Text(
                    "insure you above 14",
                    color = Color.Red,
                    fontSize = 14.ssp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            DropDownGender(
                onValueChange = {
                    onEvent(SignUpEvents.OnTyping(Typing.GENDER, it))
                }
            )
            Spacer(Modifier.height(16.sdp))
            OutlinedButton(
                onClick = {
                    onEvent(SignUpEvents.OnSignUp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    "Sign Up",
                    fontSize = 17.ssp
                )
            }
        }
        item {
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SingUpPrev() {
    TeamMangerTheme {
        SignUpScreen(
            state = SignUpState(userName = "dd", age = "14"),
            isEmailError = false to EmailErrors.IS_EMPTY,
            isPasswordError = false to PasswordErrors.IS_EMPTY,
            isPhoneError = false to PhoneNumberErrors.IS_EMPTY
        ) {

        }
    }
}