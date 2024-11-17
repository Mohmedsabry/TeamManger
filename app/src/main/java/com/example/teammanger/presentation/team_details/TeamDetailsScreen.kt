@file:OptIn(ExperimentalLayoutApi::class)

package com.example.teammanger.presentation.team_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.domain.model.Task
import com.example.teammanger.R
import com.example.teammanger.presentation.component.ImageLoaderComponent
import com.example.teammanger.presentation.component.TaskComponent
import com.example.teammanger.presentation.component.TaskView
import com.example.teammanger.ui.theme.blackOrWhite
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlin.random.Random

@Composable
fun TeamDetailsScreen(
    state: TeamDetailsState, onEvent: (TeamDetailsEvent) -> Unit
) {
    val expendedOverflow = ContextualFlowRowOverflow.expandIndicator {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${state.team.members.size - this@expandIndicator.shownItemCount}",
                color = Color.Black
            )
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.sdp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = state.team.name,
            fontSize = 17.ssp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = blackOrWhite,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Project Details", fontSize = 14.ssp, color = blackOrWhite
        )
        Text(
            text = state.team.description,
            fontSize = 12.ssp,
            overflow = TextOverflow.Clip,
            color = blackOrWhite
        )
        Text(
            text = "Members", fontSize = 14.ssp, color = blackOrWhite
        )
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            ContextualFlowRow(
                itemCount = state.team.members.size,
                maxItemsInEachRow = 4,
                maxLines = 1,
                overflow = expendedOverflow
            ) { index ->
                val member = state.team.members[index]
                Box(
                    modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center
                ) {
                    if (state.isAdmin) Icon(imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .clickable {
                                onEvent(TeamDetailsEvent.DeleteMember(member = member))
                            }
                            .align(Alignment.TopEnd))
                    ImageLoaderComponent(
                        image = member.image, gender = member.gender
                    )
                }
            }
            if (state.isAdmin) {
                IconButton(
                    onClick = {
                        onEvent(TeamDetailsEvent.AddMember)
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.addsquare), contentDescription = null
                    )
                }
            }
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Project Progress", fontSize = 12.ssp, color = blackOrWhite
            )
            Box(
                modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center
            ) {
                Text("${(state.done.toFloat() / state.team.tasks.size.toFloat()) * 100}%")
                CircularProgressIndicator(
                    progress = {
                        state.done.toFloat() / state.team.tasks.size.toFloat()
                    }, color = Color(Random.nextInt()),
                    modifier = Modifier.fillMaxSize(),
                    trackColor = Color.LightGray
                )
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state.isAdmin) {
                true -> {
                    items(state.team.tasks, key = { it.id }) { item: Task ->
                        TaskView(task = item) {
                            onEvent(TeamDetailsEvent.DeleteTask(item))
                        }
                    }
                    item {
                        IconButton(
                            onClick = {
                                onEvent(TeamDetailsEvent.AddTask)
                            },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.addsquare),
                                contentDescription = null
                            )
                        }
                    }
                }

                false -> {
                    items(state.team.tasks, key = { it.id }) { task ->
                        TaskComponent(
                            task = task, onCheckedClicked = {
                                onEvent(TeamDetailsEvent.OnDoneTask(task, it))
                            }, memberEmail = state.email
                        )
                    }
                }
            }
        }
    }
}