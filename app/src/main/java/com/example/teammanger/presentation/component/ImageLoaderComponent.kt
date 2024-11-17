package com.example.teammanger.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.teammanger.R
import com.example.teammanger.util.Constants.FEMALE
import java.util.Base64

@SuppressLint("NewApi")
@Composable
fun ImageLoaderComponent(
    image: String,
    gender: String,
    size: Dp = 50.dp
) {
    println("image $image")
    val decoder = Base64.getDecoder()
    println("decoder $decoder")
    AsyncImage(
        model = if (image == "no image") {
            if (gender == FEMALE) LocalContext.current.resources.getDrawable(
                R.drawable.avatar_girl
            ) else LocalContext.current.resources.getDrawable(
                R.drawable.avatar_boy
            )
        } else decoder.decode(image),
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
    )
}
