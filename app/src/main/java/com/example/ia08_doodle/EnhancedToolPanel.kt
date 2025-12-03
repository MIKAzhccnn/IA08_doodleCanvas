package com.example.ia08_doodle

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun EnhancedToolPanel(
    currentColor: Color,
    currentWidth: Float,
    onColorChange: (Color) -> Unit,
    onWidthChange: (Float) -> Unit,
    onClear: () -> Unit,
    onUndo: () -> Unit,
    canUndo: Boolean,
    isDrawing: Boolean
) {
    val colors = listOf(
        Color(0xFF000000),
        Color(0xFFFF0000),
        Color(0xFF0000FF),
        Color(0xFF00FF00),
        Color(0xFFFFFF00),
        Color(0xFFFF00FF),
        Color(0xFF00FFFF),
        Color(0xFFFFFFFF),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stroke Setting",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = if (isDrawing) "Drawing..." else "Ready",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isDrawing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Color Selection",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))


            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 4
            ) {
                colors.forEach { color ->
                    val isSelected = color == currentColor
                    val borderWidth = if (isSelected) 3.dp else 0.dp

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .combinedClickable(
                                onClick = { onColorChange(color) },
                                onLongClick = {
                                }
                            )
                            .border(
                                width = borderWidth,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        shape = CircleShape
                                    )
                            )
                        }

                        if (color == Color.White) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Size",
                    style = MaterialTheme.typography.labelLarge
                )

                Text(
                    text = "${currentWidth.toInt()}px",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Slider(
                value = currentWidth,
                onValueChange = { newWidth ->
                    if (!isDrawing) {
                        onWidthChange(newWidth.coerceIn(1f, 50f))
                    }
                },
                valueRange = 1f..50f,
                steps = 48,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isDrawing
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(currentWidth.dp)
                        .background(currentColor, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(
                    icon = Icons.Default.Clear,
                    label = "undo",
                    onClick = onUndo,
                    enabled = canUndo && !isDrawing,
                    tint = MaterialTheme.colorScheme.secondary
                )

                ActionButton(
                    icon = Icons.Default.Delete,
                    label = "clear",
                    onClick = onClear,
                    enabled = !isDrawing,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    tint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (enabled) tint else tint.copy(alpha = 0.38f),
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}