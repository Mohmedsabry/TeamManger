package com.example.teammanger.presentation.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.teammanger.R
import com.example.teammanger.ui.theme.TeamMangerTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun OnBoardingComponent(
    item: OnBoardingItem
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.sdp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.sdp)
    ) {
        Image(
            painter = painterResource(id = item.image),
            contentScale = ContentScale.Crop,
            contentDescription = item.name,
            modifier = Modifier
                .size(300.sdp)
                .padding(8.sdp)
        )
        Text(
            text = item.name,
            fontSize = 15.ssp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.desc,
            fontSize = 12.ssp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Serif
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "night mode"
)
@Composable
private fun ComponentPrev() {
    TeamMangerTheme {
        OnBoardingComponent(
            OnBoardingItem(
                "Create Task",
                "create tasks quickly and easily, this smart tool is designed to help you better manage your task.",
                image = R.drawable.pana
            )
        )
    }
}