package com.example.teammanger.presentation.component

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.teammanger.R
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.ui.theme.blackOrWhite
import com.example.teammanger.util.Constants.FEMALE
import com.example.teammanger.util.Constants.MALE
import ir.kaaveh.sdpcompose.ssp
import java.util.Base64

@SuppressLint("NewApi")
@Composable
fun ChatComponent(
    img: String,
    name: String,
    lastMessage: String,
    gender: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(15.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ImageLoaderComponent(
            image = img,
            gender=gender
        )
        Column(
            Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                name.capitalize(LocaleList()),
                fontSize = 12.ssp,
                fontWeight = FontWeight.W500,
                color = blackOrWhite
            )
            Text(
                text = lastMessage.ifEmpty { "hi, please send message...." }
                    .capitalize(LocaleList()),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.ssp,
                color = blackOrWhite.copy(alpha = .8f),
                fontWeight = FontWeight.W400
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChatPrev() {
    TeamMangerTheme {
        ChatComponent(
            img = "",
            "ahmed",
            "",
            MALE,
            onClick = {}
        )
    }
}