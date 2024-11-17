package com.example.teammanger.presentation.component

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.Member
import com.example.domain.model.Task
import com.example.domain.model.Team
import com.example.domain.util.TaskStatue
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.util.Constants.FEMALE
import ir.kaaveh.sdpcompose.ssp
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeamComponent(
    modifier: Modifier = Modifier,
    team: Team,
    onTeamClicked: (Team) -> Unit = {}
) {
    val successFullTasks by remember {
        mutableIntStateOf(team.tasks.count { it.status == TaskStatue.DONE.name.lowercase() })
    }
    val contextualFlowCount = ContextualFlowRowOverflow.expandIndicator {
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "+${team.members.size - this@expandIndicator.shownItemCount}",
                color = Color.Black,
            )
        }
    }
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { onTeamClicked(team) },
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth(.5f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    team.name,
                    fontSize = 16.ssp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.background
                )
                Text(
                    team.description,
                    fontSize = 14.ssp,
                    fontWeight = FontWeight.W500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.background
                )
                Text(
                    "team",
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.background
                )
                ContextualFlowRow(
                    itemCount = team.members.size,
                    overflow = contextualFlowCount,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    maxLines = 1,
                    maxItemsInEachRow = 4
                ) { index ->
                    ImageLoaderComponent(
                        team.members[index].image,
                        team.members[index].gender,
                        size = 25.dp
                    )
                }
                Row {
                    Icon(
                        imageVector = Icons.Default.DateRange, contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        team.timeCreatedPattern(),
                        fontSize = 12.ssp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
            Column(
                Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                CircularProgressIndicator(
                    progress = {
                        successFullTasks / team.tasks.size.toFloat()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    color = Color(Random.nextInt()).copy(alpha = 1f),
                    trackColor = Color.LightGray,
                )
                Text(
                    "${team.tasks.size} Tasks",
                    fontSize = 14.ssp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

@SuppressLint("NewApi")
@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TeamPrev() {
    val context = LocalContext.current
    TeamMangerTheme {
        TeamComponent(
            team = Team(
                name = "Wi-Fi analyzer",
                description = "mobile app",
                admin = "mohamed",
                members = listOf(
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = "male"
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = "male"
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = FEMALE
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = FEMALE
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = FEMALE
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = FEMALE
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = FEMALE
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = FEMALE
                    ),
                    Member(
                        email = "",
                        tasks = listOf(),
                        teams = listOf(),
                        name = "",
                        image = "",
                        gender = FEMALE
                    ),
                ),
                tasks = listOf(
                    Task(
                        id = "",
                        creator = "",
                        description = "",
                        due = "",
                        status = TaskStatue.DONE.name.lowercase(),
                        deadLine = 1,
                        createdTime = 1,
                        teamId = ""
                    ),
                    Task(
                        id = "",
                        creator = "",
                        description = "",
                        due = "",
                        status = TaskStatue.DONE.name.lowercase(),
                        deadLine = 1,
                        createdTime = 1,
                        teamId = ""
                    ),
                    Task(
                        id = "",
                        creator = "",
                        description = "",
                        due = "",
                        status = TaskStatue.IN_PROGRESS.name.lowercase(),
                        deadLine = 1,
                        createdTime = 1,
                        teamId = ""
                    ),
                    Task(
                        id = "",
                        creator = "",
                        description = "",
                        due = "",
                        status = TaskStatue.IN_PROGRESS.name.lowercase(),
                        deadLine = 1,
                        createdTime = 1,
                        teamId = ""
                    ),
                    Task(
                        id = "",
                        creator = "",
                        description = "",
                        due = "",
                        status = TaskStatue.DONE.name.lowercase(),
                        deadLine = 1,
                        createdTime = 1,
                        teamId = ""
                    ),
                    Task(
                        id = "",
                        creator = "",
                        description = "",
                        due = "",
                        status = TaskStatue.DONE.name.lowercase(),
                        deadLine = 1,
                        createdTime = 1,
                        teamId = ""
                    ),
                ),
                timeCreated = System.currentTimeMillis()
            )
        )
    }
}