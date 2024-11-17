@file:OptIn(ExperimentalFoundationApi::class)

package com.example.teammanger.presentation.chat_history

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.ChatHistory
import com.example.teammanger.presentation.component.ChatComponent
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.ui.theme.blackOrWhite
import com.example.teammanger.ui.theme.selectedNavigationColor
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

@Composable
fun ChatHistoryScreen(
    state: ChatHistoryState,
    onEvent: (ChatHistoryEvents) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) {
        2
    }
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 2) {
            focusRequester.requestFocus()
        }
    }
    HorizontalPager(
        pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.sdp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.sdp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.sdp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                OutlinedCard(
                    onClick = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "History",
                        fontSize = 15.ssp,
                        color = blackOrWhite,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    LinearProgressIndicator(
                        color = if (0 == pagerState.currentPage) selectedNavigationColor else Color.LightGray,
                        strokeCap = StrokeCap.Round,
                        trackColor = Color.LightGray,
                        progress = { 1f }
                    )
                }
                OutlinedCard(
                    onClick = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }, modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Search",
                        fontSize = 15.ssp,
                        color = blackOrWhite,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    LinearProgressIndicator(
                        color = if (1 == pagerState.currentPage) selectedNavigationColor else Color.LightGray,
                        strokeCap = StrokeCap.Round,
                        trackColor = Color.LightGray,
                        progress = { 1f }
                    )
                }
            }
            when (it) {
                0 -> {
                    LazyColumn(
                        Modifier.weight(1f),
                    ) {
                        items(state.chatHistory) { chat ->
                            ChatComponent(
                                img = chat.img,
                                name = chat.receiver.name,
                                lastMessage = chat.lastMessage,
                                gender = chat.receiver.gender,
                                onClick = {
                                    onEvent(ChatHistoryEvents.OnChatClicked(chat))
                                }
                            )
                        }
                    }
                }

                1 -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            5.dp,
                            Alignment.CenterHorizontally
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { onEvent(ChatHistoryEvents.OnTyping(it)) },
                            placeholder = {
                                Text("Search", color = blackOrWhite)
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier.focusRequester(focusRequester),
                            singleLine = true
                        )
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onEvent(ChatHistoryEvents.OnSearchClicked)
                            },
                            tint = blackOrWhite
                        )
                    }
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                    ) {
                        items(state.filteredList) { member ->
                            ChatComponent(
                                img = member.image,
                                name = member.name,
                                lastMessage = "",
                                gender = member.gender,
                                onClick = {
                                    onEvent(
                                        ChatHistoryEvents.OnChatClicked(
                                            ChatHistory(
                                                sender = state.email,
                                                receiver = member,
                                                img = member.image,
                                                lastMessage = ""
                                            )
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChatPrev() {
    TeamMangerTheme {
        ChatHistoryScreen(
            state = ChatHistoryState(),
            onEvent = {}
        )
    }
}