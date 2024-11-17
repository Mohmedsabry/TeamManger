@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.example.teammanger.presentation.add_team

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.Task
import com.example.domain.util.TaskStatue
import com.example.teammanger.R
import com.example.teammanger.presentation.component.BottomSheet
import com.example.teammanger.presentation.component.MembersComponent
import com.example.teammanger.presentation.component.TaskView
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.util.Constants.FEMALE
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
@Composable
fun AddTeamScreen(
    state: AddTeamState,
    taskName: String,
    due: String,
    name: String,
    description: String,
    onEvent: (AddTeamEvent) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    val bottomSheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Scaffold(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        topBar = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp)
                        .padding(16.dp)
                        .background(Color.LightGray, CircleShape)
                        .clickable {
                            onEvent(AddTeamEvent.OnNavigationBack)
                        },
                    tint = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp)
                        .clickable { onEvent(AddTeamEvent.OnAddTeam) }
                        .padding(16.dp)
                )
            }
        }
    ) { inner ->
        BottomSheet(
            desc = taskName,
            emails = state.dues,
            sheetState = bottomSheet,
            onDateChange = { onEvent(AddTeamEvent.OnDateChange(it)) },
            onDescChange = { onEvent(AddTeamEvent.OnDescChange(it)) },
            onDueChange = { onEvent(AddTeamEvent.OnDueChange(it)) },
            onAddTask = {
                if (due.isNotEmpty() && taskName.isNotEmpty()) {
                    onEvent(AddTeamEvent.OnAddTask)
                    coroutine.launch {
                        bottomSheet.hide()
                    }
                }
            }
        )
        if (state.showMembers) {
            MembersComponent(
                members = state.members,
                onCheckedClicked = { isChecked, member ->
                    onEvent(
                        AddTeamEvent.OnAddMember(
                            isChecked,
                            member
                        )
                    )
                },
                onDismiss = { onEvent(AddTeamEvent.OnShowMembers(false)) },
                selectedMembers = state.selectedMembers
            )
        }
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(
                5.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { onEvent(AddTeamEvent.OnTypingTeamName(it)) },
                    label = { Text("Name") },
                    isError = name.isEmpty(),
                    modifier = Modifier.focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
                Spacer(Modifier.height(5.dp))
                AnimatedVisibility(name.isEmpty()) {
                    Text(
                        "Team must have a name",
                        fontSize = 15.ssp,
                        color = Color.Red
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { onEvent(AddTeamEvent.OnTypingTeamDesc(it)) },
                    label = { Text("Description") },
                    isError = description.isEmpty(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus(force = true)
                        }
                    )
                )
                Spacer(Modifier.height(5.dp))
                AnimatedVisibility(description.isEmpty()) {
                    Text(
                        "Team must have a desc",
                        fontSize = 15.ssp,
                        color = Color.Red
                    )
                }
            }
            item {
                val overFlow = ContextualFlowRowOverflow.expandIndicator {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "+${state.members.size - this@expandIndicator.shownItemCount}",
                                color = Color.Black,
                                fontSize = 15.ssp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.addsquare),
                                null,
                                modifier = Modifier
                                    .clickable {
                                        onEvent(AddTeamEvent.OnShowMembers(true))
                                    }
                                    .padding(5.dp),
                                tint = Color.Black
                            )
                        }
                    }
                }
                if (state.selectedMembers.isNotEmpty()) {
                    ContextualFlowRow(
                        itemCount = state.selectedMembers.size,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.sdp, vertical = 5.sdp),
                        maxLines = 1,
                        maxItemsInEachRow = 2,
                        overflow = overFlow,
                        verticalArrangement = Arrangement.Center
                    ) { index ->
                        val decoder = java.util.Base64.getDecoder()
                        AsyncImage(
                            model = if (state.selectedMembers[index].image == "no image") {
                                if (state.selectedMembers[index].gender == FEMALE) LocalContext.current.resources.getDrawable(
                                    R.drawable.avatar_girl
                                ) else LocalContext.current.resources.getDrawable(
                                    R.drawable.avatar_boy
                                )
                            } else decoder.decode(
                                state.selectedMembers[index].image
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                        )
                    }
                }
                if (state.selectedMembers.size < 2) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.addsquare),
                            null,
                            modifier = Modifier
                                .clickable {
                                    onEvent(AddTeamEvent.OnShowMembers(true))
                                }
                                .padding(5.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
            items(state.tasks) { task ->
                TaskView(
                    task = task,
                    onDelete = { onEvent(AddTeamEvent.OnDeleteTask(it)) }
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.addsquare),
                        null,
                        modifier = Modifier
                            .clickable {
                                coroutine.launch {
                                    bottomSheet.show()
                                }
                            }
                            .padding(5.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddPrev() {
    TeamMangerTheme {
        AddTeamScreen(
            state = AddTeamState(
                members = listOf(),
                tasks = listOf(
                    Task(
                        id = "",
                        creator = "",
                        description = "task1",
                        due = "mo",
                        status = TaskStatue.DONE.name.lowercase(),
                        deadLine = System.currentTimeMillis(),
                        createdTime = 1,
                        teamId = ""
                    ),
                    Task(
                        id = "",
                        creator = "",
                        description = "task2",
                        due = "mo2",
                        status = TaskStatue.DONE.name,
                        deadLine = System.currentTimeMillis(),
                        createdTime = 1,
                        teamId = ""
                    ),
                    Task(
                        id = "",
                        creator = "",
                        description = "task3",
                        due = "mo3",
                        status = TaskStatue.IN_PROGRESS.name,
                        deadLine = System.currentTimeMillis(),
                        createdTime = 1,
                        teamId = ""
                    )
                ),
            ),
            onEvent = {},
            description = "",
            due = "",
            name = "",
            taskName = ""
        )
    }
}