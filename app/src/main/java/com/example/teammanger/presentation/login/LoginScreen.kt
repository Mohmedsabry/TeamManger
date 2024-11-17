package com.example.teammanger.presentation.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammanger.R
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.ui.theme.blackOrWhite
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvents) -> Unit,
    onForgetPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    var showPasswordIcon by rememberSaveable {
        mutableStateOf(false)
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.sdp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(5.sdp, Alignment.CenterVertically),
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(LoginEvents.OnTypingEmail(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
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
            singleLine = true,
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(LoginEvents.OnTypingPassword(it)) },
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
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
        )
        Text(
            "Forget Password?",
            fontSize = 14.ssp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clickable {
                    onForgetPasswordClicked()
                }
                .align(Alignment.End)
                .padding(horizontal = 5.sdp)
        )
        Spacer(Modifier.height(10.sdp))
        OutlinedButton(
            onClick = {
                onEvent(LoginEvents.OnLogin)
            }, colors = ButtonDefaults.outlinedButtonColors(
                contentColor = blackOrWhite,
                containerColor = MaterialTheme.colorScheme.background
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(5.sdp)
        ) {
            Text(
                "Login",
                fontSize = 16.ssp,
                fontWeight = FontWeight.Bold
            )
        }
        AnimatedVisibility(state.error != null) {
            state.error?.let {
                Text(
                    "Please check email or Password Again",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontSize = 14.ssp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                "Don't have account?",
                fontSize = 15.ssp,
                color = blackOrWhite
            )
            Spacer(Modifier.width(5.dp))
            Text(
                "SignUp",
                fontSize = 13.ssp,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSignUpClicked() }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginPrev() {
    TeamMangerTheme {
        LoginScreen(
            state = LoginState(),
            onEvent = {},
            onSignUpClicked = {},
            onForgetPasswordClicked = {}
        )
    }
}