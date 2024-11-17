@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.teammanger.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.Task
import com.example.domain.util.TaskStatue
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.util.toDate
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.delay

@Composable
fun TaskView(
    task: Task,
    onDelete: (Task) -> Unit
) {
    SwipeToDeleteContainer(task, onDelete) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(15.dp)),
        ) {
            Text(
                task.description.capitalize(localeList = LocaleList()),
                color = MaterialTheme.colorScheme.background,
                fontSize = 12.ssp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                task.due.capitalize(localeList = LocaleList()),
                color = MaterialTheme.colorScheme.background,
                fontSize = 10.ssp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                task.deadLine.toDate(),
                color = MaterialTheme.colorScheme.background,
                fontSize = 9.ssp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.W400,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                task.status.lowercase().capitalize(localeList = LocaleList()),
                color = if (task.status.lowercase() == TaskStatue.DONE.name.lowercase()) Color.Green else Color(
                    0xFFFA5C21
                ),
                fontSize = 10.ssp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun DeleteBackground(
    swipe: SwipeToDismissBoxState
) {
    val color =
        if (swipe.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.Red else Color.Transparent
    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(8.sdp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun SwipeToDeleteContainer(
    task: Task,
    onDelete: (Task) -> Unit,
    content: @Composable (Task) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val swipe = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )
    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(500)
            onDelete(task)
        }
    }
    AnimatedVisibility(
        !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(500),
            shrinkTowards = Alignment.Top
        ) + fadeOut(),
        enter = slideInVertically(
            animationSpec = tween(500),
            initialOffsetY = {it/2}
        ) + fadeIn()
    ) {
        SwipeToDismissBox(
            state = swipe,
            backgroundContent = {
                DeleteBackground(swipe)
            },
            enableDismissFromStartToEnd = false,
            modifier = Modifier
                .padding(8.sdp)
                .clip(RoundedCornerShape(15.dp))
        ) {
            content(task)
        }
    }
}

@Preview
@Composable
private fun TaskPrev() {
    TeamMangerTheme {
        TaskView(
            Task(
                id = "",
                createdTime = System.currentTimeMillis(),
                description = "desc",
                deadLine = System.currentTimeMillis(),
                due = "mohammed",
                status = TaskStatue.DONE.name,
                teamId = "",
                creator = ""
            )
        ) { }
    }
}