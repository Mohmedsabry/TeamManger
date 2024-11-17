package com.example.teammanger.presentation.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teammanger.ui.theme.TeamMangerTheme
import ir.kaaveh.sdpcompose.sdp

@Composable
fun OnBoardingIndicator(
    size: Int,
    selectedColor: Color = Color(0xFF6650a4),
    unSelectedColor: Color = Color.LightGray,
    selected: Int
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.sdp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(size) { index ->
            val animatedMaxWidth by animateDpAsState(
                if (index == selected) 15.dp else 5.dp,
                label = "",
                animationSpec = tween(5000)
            )
            Box(
                modifier = Modifier
                    .width(animatedMaxWidth)
                    .height(5.sdp)
                    .clip(CircleShape)
                    .background(
                        if (index == selected) selectedColor else unSelectedColor,
                    )
            )
            Spacer(Modifier.width(5.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IndicatorPrev() {
    TeamMangerTheme {
        Box(Modifier.fillMaxSize()) {
            OnBoardingIndicator(
                size = 4,
                selectedColor = Color(0xFF6650a4),
                unSelectedColor = Color.LightGray,
                1
            )
        }
    }
}