package com.example.teammanger.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.teammanger.ui.theme.blackOrWhite
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun NotificationScreen(
    state: NotificationState,
    onEvent: (NotificationEvents) -> Unit
) {
    val refreshState = rememberSwipeRefreshState(false)
    SwipeRefresh(
        state = refreshState,
        onRefresh = {
            onEvent(NotificationEvents.OnRefresh)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.sdp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                "clearNotification",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        onEvent(NotificationEvents.OnClearNotification)
                    }
                    .padding(5.sdp),
                fontSize = 14.ssp,
                color = Color.LightGray
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.sdp)
            ) {
                items(state.notifications) { notification ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.sdp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text(
                            text = notification.who,
                            fontSize = 17.ssp,
                            color = blackOrWhite,
                        )
                        Text(
                            text = notification.message,
                            fontSize = 15.ssp,
                            color = blackOrWhite
                        )
                    }
                }
            }
        }
    }
}