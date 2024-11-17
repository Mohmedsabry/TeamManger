package com.example.teammanger.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammanger.ui.theme.TeamMangerTheme
import com.example.teammanger.ui.theme.blackOrWhite
import com.example.teammanger.util.toDate
import ir.kaaveh.sdpcompose.ssp
import okhttp3.internal.addHeaderLenient

@Composable
fun MessageComponent(
    img: String,
    name: String,
    message: String,
    gender: String,
    time: Long,
    isOwnMessage: Boolean = true,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(
                if (isOwnMessage) Color.Green.copy(.7f) else Color.DarkGray,
                RoundedCornerShape(15.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        ImageLoaderComponent(
            image = img,
            gender = gender
        )
        Column(
            Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                name.capitalize(LocaleList()),
                fontSize = 12.ssp,
                fontWeight = FontWeight.W500,
                color = if (isOwnMessage)Color.Black else Color.White
            )
            Text(
                text = message.ifEmpty { "hi, please send message...." }
                    .capitalize(LocaleList()),
                overflow = TextOverflow.Clip,
                fontSize = 10.ssp,
                color = if (isOwnMessage)Color.Black else Color.White,
                fontWeight = FontWeight.W400
            )
            Text(
                text = time.toDate(),
                fontSize = 10.ssp,
                color = if (isOwnMessage)Color.Black else Color.White,
                fontWeight = FontWeight.W400,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MessagePrev() {
    TeamMangerTheme {
        MessageComponent(
            img = "no image",
            name = "ali",
            "hipleaplaplaplaplaplapalpplappppplaplaplapplapp",
            gender = "male",
            time = System.currentTimeMillis()
        )
    }
}