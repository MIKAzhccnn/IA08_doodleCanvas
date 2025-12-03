package com.example.ia08_doodle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke as DrawStroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

@Composable
fun EnhancedCanvas(
    strokes: List<Stroke>,
    currentPoints: MutableList<Offset>,
    currentBrushSettings: BrushSettings,
    isDrawing: Boolean,
    onDrawingStateChange: (Boolean) -> Unit,
    onStrokeComplete: (Stroke) -> Unit
) {

    var currentStrokeInfo by remember {
        mutableStateOf<Pair<Color, Float>?>(null)
    }


    LaunchedEffect(currentBrushSettings.color, currentBrushSettings.width) {
        if (!isDrawing) {
            currentStrokeInfo = Pair(currentBrushSettings.color, currentBrushSettings.width)
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(currentBrushSettings) {
                detectDragGestures(
                    onDragStart = { offset ->

                        val color = currentBrushSettings.color
                        val width = currentBrushSettings.width


                        currentStrokeInfo = Pair(color, width)


                        onDrawingStateChange(true)
                        currentPoints.clear()
                        currentPoints.add(offset)
                    },
                    onDrag = { change, _ ->
                        currentPoints.add(change.position)
                    },
                    onDragEnd = {

                        currentStrokeInfo?.let { (color, width) ->
                            if (currentPoints.size > 1) {
                                onStrokeComplete(
                                    Stroke(
                                        points = currentPoints.toList(),
                                        color = color,
                                        strokeWidth = width
                                    )
                                )
                            }
                        }


                        currentStrokeInfo = null
                        currentPoints.clear()
                        onDrawingStateChange(false)
                    }
                )
            }
    ) {

        strokes.forEach { stroke ->
            drawStroke(stroke)
        }


        currentStrokeInfo?.let { (color, width) ->
            if (currentPoints.size > 1) {
                val path = Path().apply {
                    moveTo(currentPoints[0].x, currentPoints[0].y)
                    for (i in 1 until currentPoints.size) {
                        lineTo(currentPoints[i].x, currentPoints[i].y)
                    }
                }

                drawPath(
                    path = path,
                    color = color,
                    style = DrawStroke(
                        width = width,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round,
                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                    )
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStroke(stroke: Stroke) {
    if (stroke.points.size < 2) return

    val path = Path().apply {
        moveTo(stroke.points[0].x, stroke.points[0].y)
        stroke.points.forEachIndexed { index, point ->
            if (index > 0) {
                lineTo(point.x, point.y)
            }
        }
    }

    drawPath(
        path = path,
        color = stroke.color,
        style = DrawStroke(
            width = stroke.strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
            join = androidx.compose.ui.graphics.StrokeJoin.Round
        )
    )
}