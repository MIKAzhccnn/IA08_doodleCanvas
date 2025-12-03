package com.example.ia08_doodle

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb

const val MAX_STROKES = 500

data class Stroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val id: Long = System.currentTimeMillis()
) {
    fun toPath(): Path {
        val path = Path()
        if (points.isNotEmpty()) {
            path.moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                path.lineTo(points[i].x, points[i].y)
            }
        }
        return path
    }
}

data class BrushSettings(
    val color: Color = Color.Black,
    val width: Float = 10f
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "color" to color.toArgb(),
            "width" to width
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): BrushSettings {
            return BrushSettings(
                color = Color(map["color"] as Int),
                width = (map["width"] as? Float) ?: 10f
            )
        }
    }
}