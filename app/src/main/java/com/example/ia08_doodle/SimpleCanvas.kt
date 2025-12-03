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

@Composable
fun SimpleCanvas(
    strokes: List<Stroke>,
    currentPoints: MutableList<Offset>,
    currentBrushSettings: BrushSettings,
    isDrawing: Boolean,
    onDrawingStateChange: (Boolean) -> Unit,
    onStrokeComplete: (Stroke) -> Unit
) {
    var capturedBrushSettings by remember { mutableStateOf<BrushSettings?>(null) }
    
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(currentBrushSettings) {
                detectDragGestures(
                    onDragStart = { offset ->
                        capturedBrushSettings = BrushSettings(
                            color = currentBrushSettings.color,
                            width = currentBrushSettings.width
                        )
                        
                        onDrawingStateChange(true)
                        currentPoints.clear()
                        currentPoints.add(offset)
                    },
                    onDrag = { change, _ ->
                        currentPoints.add(change.position)
                    },
                    onDragEnd = {
                        capturedBrushSettings?.let { brush ->
                            if (currentPoints.size > 1) {
                                onStrokeComplete(
                                    Stroke(
                                        points = currentPoints.toList(),
                                        color = brush.color,
                                        strokeWidth = brush.width
                                    )
                                )
                            }
                        }

                        capturedBrushSettings = null
                        currentPoints.clear()
                        onDrawingStateChange(false)
                    }
                )
            }
    ) {
        strokes.forEach { stroke ->
            drawStroke(stroke)
        }

        capturedBrushSettings?.let { brush ->
            if (currentPoints.size > 1) {
                val path = Path().apply {
                    moveTo(currentPoints[0].x, currentPoints[0].y)
                    for (i in 1 until currentPoints.size) {
                        lineTo(currentPoints[i].x, currentPoints[i].y)
                    }
                }
                
                drawPath(
                    path = path,
                    color = brush.color,
                    style = DrawStroke(
                        width = brush.width,
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
        for (i in 1 until stroke.points.size) {
            lineTo(stroke.points[i].x, stroke.points[i].y)
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