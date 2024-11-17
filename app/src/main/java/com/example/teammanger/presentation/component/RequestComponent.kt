package com.example.teammanger.presentation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RequestComponent(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    onExpended: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var actionsWidth by remember {
        mutableFloatStateOf(0f)
    }
    val offset = remember {
        Animatable(0f)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(isRevealed, actionsWidth) {
        if (isRevealed) {
            offset.animateTo(actionsWidth)
        } else {
            offset.animateTo(0f)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.sdp)
    ) {
        Row(
            modifier = Modifier.onSizeChanged {
                actionsWidth = it.width.toFloat()
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(offset.value.roundToInt(), 0)
                }
                .pointerInput(actionsWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(0f, actionsWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            if (offset.value >= actionsWidth / 2f) {
                                scope.launch {
                                    offset.animateTo(actionsWidth)
                                    onExpended()
                                }
                            } else {
                                scope.launch {
                                    offset.animateTo(0f)
                                    onCollapsed()
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}