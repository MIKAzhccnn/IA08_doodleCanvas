package com.example.ia08_doodle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleToolPanel(
    currentColor: Color,
    currentWidth: Float,
    onColorChange: (Color) -> Unit,
    onWidthChange: (Float) -> Unit,
    onClear: () -> Unit,
    onUndo: () -> Unit,
    hasStrokes: Boolean,
    isDrawing: Boolean
) {
    val colors = listOf(
        Color.Black, Color.Red, Color.Blue, 
        Color.Green, Color.Yellow, Color.Magenta,
        Color.Cyan, Color.White
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isDrawing) "🎨 Drawing..." else "✅ Ready",
                color = if (isDrawing) MaterialTheme.colorScheme.primary 
                       else MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Text("Colors：", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(color, CircleShape)
                            .clickable(
                                enabled = !isDrawing,
                                onClick = { onColorChange(color) }
                            )
                    ) {
                        if (color == currentColor) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Color.White.copy(alpha = 0.3f),
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Size：")
                Text("${currentWidth.toInt()}px", 
                     color = MaterialTheme.colorScheme.primary)
            }
            
            Slider(
                value = currentWidth,
                onValueChange = onWidthChange,
                valueRange = 1f..50f,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isDrawing
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onUndo,
                    enabled = hasStrokes && !isDrawing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Undo")
                }
                
                Button(
                    onClick = onClear,
                    enabled = !isDrawing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Clear")
                }
            }
        }
    }
}