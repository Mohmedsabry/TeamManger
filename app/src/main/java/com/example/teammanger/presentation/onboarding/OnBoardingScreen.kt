package com.example.teammanger.presentation.onboarding

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammanger.R
import com.example.teammanger.ui.theme.TeamMangerTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

val items = listOf(
    OnBoardingItem(
        "Create Tasks",
        "create tasks quickly and easily, this smart tool is designed to help you better manage your task.",
        R.drawable.pana
    ),
    OnBoardingItem(
        "Reminder!!",
        "you can reminder yourself about everything set reminders and never miss important things.",
        R.drawable.pana
    ),
    OnBoardingItem(
        "TO-DO List",
        "stay organized with our easy-to-use task manager,where you can create,check daily to-do list.",
        R.drawable.pana
    ),
    OnBoardingItem(
        "All In One",
        "manage all work in one place, Don't need any more apps for manage task.",
        R.drawable.pana
    ),
)

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    val isCurrentPageItLastPage by remember(pagerState.currentPage) {
        derivedStateOf {
            pagerState.currentPage == items.size - 1
        }
    }
    val coroutine = rememberCoroutineScope()
    HorizontalPager(state = pagerState) { page ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.sdp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = if (!isCurrentPageItLastPage) Arrangement.spacedBy(15.sdp) else Arrangement.spacedBy(
                15.sdp,
                Alignment.CenterVertically
            ),
        ) {
            if (!isCurrentPageItLastPage) {
                Text(
                    "Skip",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .4f),
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            coroutine.launch {
                                pagerState.animateScrollToPage(3)
                            }
                        }
                        .padding(10.sdp),
                    fontSize = 13.ssp
                )
            }
            OnBoardingComponent(item = items[page])
            OnBoardingIndicator(
                size = items.size,
                selected = page
            )
            if (!isCurrentPageItLastPage) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        coroutine.launch {
                            pagerState.animateScrollToPage(page - 1)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            coroutine.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = Color(0xFF6650a4)
                        )
                    ) {
                        Text(
                            "Next",
                            fontSize = 14.ssp
                        )
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { onLogin() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF6650a4),
                        contentColor = Color.White
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.sdp)
                ) {
                    Text(
                        "Login"
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.sdp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(15.dp))
                        .clickable { onSignUp() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sign up", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingScreenPrev() {
    TeamMangerTheme {
        OnBoardingScreen(
            onLogin = {},
            onSignUp = {}
        )
    }
}