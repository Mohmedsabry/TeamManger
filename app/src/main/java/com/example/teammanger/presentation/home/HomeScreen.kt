package com.example.teammanger.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.Team
import com.example.teammanger.R
import com.example.teammanger.presentation.component.BottomNavigationBar
import com.example.teammanger.presentation.component.TeamComponent
import com.example.teammanger.presentation.ui_model.NavigationItem
import com.example.teammanger.ui.theme.TeamMangerTheme
import ir.kaaveh.sdpcompose.sdp

@Composable
fun HomeScreen(
    state: HomeState,
    onTeamClicked: (Team) -> Unit,
    onNavigationClicked: (NavigationItem) -> Unit,
    onAddTeam: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationBar { onNavigationClicked(it) } },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTeam,
                modifier = Modifier.offset(y = 40.sdp),
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(
                    painter = painterResource(R.drawable.addsquare),
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(state.teams, key = { item: Team -> item.id }) { team ->
                TeamComponent(
                    team = team,
                    onTeamClicked = { onTeamClicked(team) }
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomePrev() {
    TeamMangerTheme {
        HomeScreen(state = HomeState(), {}, {}) { }
    }
}