package com.example.teammanger.presentation.chat

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.teammanger.R
import com.example.teammanger.presentation.component.MessageComponent
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.ui.theme.blackOrWhite
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ChatScreen(
    state: ChatState,
    onEvent: (ChatEvents) -> Unit,
    connect: () -> Unit,
    disConnect: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                connect()
            } else if (event == Lifecycle.Event.ON_STOP) {
                disConnect()
            }
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose {
            lifecycle.lifecycle.removeObserver(observer)
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.sdp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(state.messages) { message ->
                val isOwnMessage = message.user.email != state.receiver
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.sdp),
                    contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    MessageComponent(
                        img = message.user.img,
                        name = message.user.name,
                        message = message.message,
                        gender = message.user.gender,
                        time = message.time,
                        isOwnMessage = isOwnMessage
                    )
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.sdp),
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.message,
                onValueChange = { onEvent(ChatEvents.OnTyping(it)) },
                label = { Text("Message", color = blackOrWhite) },
            )
            Icon(
                painter = painterResource(R.drawable.send),
                contentDescription = null,
                modifier = Modifier
                    .size(50.sdp)
                    .clickable { onEvent(ChatEvents.OnSend) },
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChatPrev() {
    TeamMangerTheme {
        ChatScreen(
            ChatState(),
            {},
            {}
        ) { }
    }
}