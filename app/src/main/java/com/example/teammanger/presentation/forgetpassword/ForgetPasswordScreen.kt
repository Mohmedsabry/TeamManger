package com.example.teammanger.presentation.forgetpassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.teammanger.R
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ForgetPasswordScreen(
    state: ForgetPasswordState,
    onEvents: (ForgetPasswordEvents) -> Unit
) {
    var showPasswordIcon by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.sdp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading)
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        AnimatedVisibility(
            visible = !state.sentEmail,
            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                animationSpec = tween(
                    500
                )
            ) { -it },
            exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(
                animationSpec = tween(
                    500
                )
            ) { it }
        ) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.sdp)
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onEvents(ForgetPasswordEvents.OnTypingEmail(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.sdp),
                    trailingIcon = { Icon(painter = painterResource(R.drawable.usertag), null) },
                    label = { Text("Email") }
                )
                AnimatedVisibility(visible = state.error != null) {
                    Text(
                        state.error!!,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedButton(
                    onClick = { onEvents(ForgetPasswordEvents.OnSendingEmail) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.sdp),
                    enabled = !state.isLoading
                ) {
                    Text("Send Email")
                }
            }
        }
        AnimatedVisibility(
            visible = state.checkedCode,
            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                animationSpec = tween(
                    500
                )
            ) { -it },
            exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(
                animationSpec = tween(
                    500
                )
            ) { it }
        ) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.sdp)
            ) {
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onEvents(ForgetPasswordEvents.OnTypingPassword(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.sdp),
                    label = { Text("new Password") },
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
                )
                AnimatedVisibility(visible = state.error != null) {
                    Text(
                        state.error!!,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedButton(
                    onClick = { onEvents(ForgetPasswordEvents.OnUpdatePassword) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.sdp),
                ) {
                    Text("update password!")
                }
            }
        }
        AnimatedVisibility(visible = !state.checkedCode && state.sentEmail,
            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                animationSpec = tween(
                    500
                )
            ) { -it },
            exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(
                animationSpec = tween(
                    500
                )
            ) { it }
        ) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.sdp)
            ) {
                Text(
                    "Check your Mail",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.ssp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.code,
                    onValueChange = { onEvents(ForgetPasswordEvents.OnTypingCode(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.sdp),
                    trailingIcon = { Icon(painter = painterResource(R.drawable.usertag), null) },
                    label = { Text("Code") }
                )
                AnimatedVisibility(visible = state.error != null) {
                    Text(
                        state.error ?: "",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        fontSize = 14.ssp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedButton(
                    onClick = { onEvents(ForgetPasswordEvents.OnCheckCode) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.sdp),
                ) {
                    Text("verify Your Code")
                }
            }
        }
    }
}