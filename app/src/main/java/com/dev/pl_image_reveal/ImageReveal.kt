package com.dev.pl_image_reveal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

@Composable
fun ImageReveal(
    modifier: Modifier = Modifier
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.me)
    var circlePos by remember {
        mutableStateOf(Offset.Zero)
    }
    var oldCirclePos by remember {
        mutableStateOf(Offset.Zero)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectDragGestures(
                    onDragEnd = {
                        oldCirclePos = circlePos
                    }
                ) { change, _ ->
                    circlePos = oldCirclePos + change.position
                }
            }
    ) {
        val bmpHeight = ((imageBitmap.height.toFloat() / imageBitmap.width.toFloat()) * size.width).roundToInt()

        val clipPath = Path().apply {
            addArc(
                oval = Rect(
                    center = circlePos,
                    radius = 200f
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 360f
            )
        }

        drawImage(
            image = imageBitmap,
            dstSize = IntSize(
                size.width.roundToInt(), bmpHeight
            ),
            dstOffset = IntOffset(
                0,
                center.y.roundToInt() - bmpHeight/2
            ),
            colorFilter = ColorFilter.tint(
                color = Color.Black,
                blendMode = BlendMode.Color
            )
        )

        clipPath(
            path = clipPath,
            clipOp = ClipOp.Intersect
        ) {
            drawImage(
                image = imageBitmap,
                dstSize = IntSize(
                    size.width.roundToInt(), bmpHeight
                ),
                dstOffset = IntOffset(
                    0,
                    center.y.roundToInt() - bmpHeight/2
                )
            )
        }
    }
}