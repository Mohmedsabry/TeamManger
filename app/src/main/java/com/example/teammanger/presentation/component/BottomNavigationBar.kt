package com.example.teammanger.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammanger.R
import com.example.teammanger.presentation.ui_model.NavigationBarItem
import com.example.teammanger.presentation.ui_model.NavigationItem
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.ui.theme.selectedNavigationColor
import com.example.teammanger.ui.theme.unSelectedNavigationColor
import ir.kaaveh.sdpcompose.sdp

val navigationItems = listOf(
    NavigationBarItem(
        "Home",
        R.drawable.home2
    ),
    NavigationBarItem(
        "Chat",
        R.drawable.messages1
    ),
    NavigationBarItem(
        "Tasks",
        R.drawable.usertag
    ),
    NavigationBarItem(
        "Notification",
        R.drawable.notification1
    ),
)

@Composable
fun BottomNavigationBar(
    selectedItem: Int = 0,
    onClick: (NavigationItem) -> Unit
) {
    NavigationBar {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    when (index) {
                        0 -> onClick(NavigationItem.HOME)
                        1 -> onClick(NavigationItem.CHAT)
                        2 -> onClick(NavigationItem.TASK)
                        else -> onClick(NavigationItem.NOTIFICATION)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                        tint = if (selectedItem == index) selectedNavigationColor else unSelectedNavigationColor,
                        modifier = Modifier.size(20.sdp)
                    )
                },
                label = {
                    Text(
                        item.title,
                        color = if (selectedItem == index) selectedNavigationColor else unSelectedNavigationColor
                    )
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview()
@Composable
private fun NavPrev() {
    TeamMangerTheme {
        BottomNavigationBar {

        }
    }
}