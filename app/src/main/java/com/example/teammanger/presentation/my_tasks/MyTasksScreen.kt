package com.example.teammanger.presentation.my_tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.teammanger.presentation.component.TaskComponent
import com.example.teammanger.ui.theme.blackOrWhite
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun MyTasksScreen(
    state: MyTaskState, onEvent: (MyTaskEvents) -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(false)
    SwipeRefresh(swipeRefreshState, onRefresh = { onEvent(MyTaskEvents.OnRefresh) }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.sdp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth().padding(top = 5.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "total: ${state.tasks.size}", fontSize = 15.ssp, color = blackOrWhite
                )
                Text(
                    text = "done: ${state.done}", fontSize = 15.ssp, color = Color.Green
                )
                Text(
                    text = "expired: ${state.expired}", fontSize = 15.ssp, color = Color.Red
                )
                Text(
                    text = "inProgress: ${state.inProgress}",
                    fontSize = 15.ssp,
                    color = Color.LightGray
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(state.tasks) { task ->
                    TaskComponent(task = task, memberEmail = state.email, onCheckedClicked = {
                        onEvent(MyTaskEvents.OnAccept(task,it))
                    })
                }
            }
        }
    }
}