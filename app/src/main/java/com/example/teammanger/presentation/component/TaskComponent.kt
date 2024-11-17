package com.example.teammanger.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.Task
import com.example.domain.util.TaskStatue
import com.example.teammanger.ui.theme.TeamMangerTheme
import ir.kaaveh.sdpcompose.ssp

@Composable
fun TaskComponent(
    modifier: Modifier = Modifier,
    task: Task,
    memberEmail: String,
    onCheckedClicked: (Boolean) -> Unit = {}
) {
    var isChecked by remember {
        mutableStateOf(false)
    }
    val currentTime by remember {
        derivedStateOf {
            System.currentTimeMillis()/1000
        }
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(5.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.onBackground),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            if (task.description.count()>28)task.description.take(26)+"..." else task.description,
            fontSize = 16.ssp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp),
            color = MaterialTheme.colorScheme.background,
            textDecoration = if (task.deadLine >= currentTime) TextDecoration.LineThrough else null,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onCheckedClicked(it)
            },
            modifier = Modifier.padding(10.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green,
                uncheckedColor = MaterialTheme.colorScheme.background
            ),
            enabled = memberEmail == task.due && task.deadLine < currentTime
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskPrev() {
    TeamMangerTheme {
        TaskComponent(
            task = Task(
                id = "",
                createdTime = System.currentTimeMillis(),
                creator = "",
                description = "User interviewsplaplaplaplaplaplapla",
                due = "mohmed",
                status = TaskStatue.DONE.name,
                deadLine = 1/1000,
                teamId = ""
            ),
            memberEmail = "mohmed"
        )
    }
}