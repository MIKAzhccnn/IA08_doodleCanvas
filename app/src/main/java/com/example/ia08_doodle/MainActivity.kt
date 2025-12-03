package com.example.ia08_doodle

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ia08_doodle.ui.theme.IA08_doodleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IA08_doodleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DoodleApp()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@SuppressLint("UnrememberedMutableState")
@Composable
fun DoodleApp() {
    val context = LocalContext.current

    val strokes = remember { mutableStateListOf<Stroke>() }

    var brushColor by remember { mutableStateOf(Color.Black) }
    var brushWidth by remember { mutableStateOf(10f) }

    val currentPoints = remember { mutableStateListOf<Offset>() }

    var isDrawing by remember { mutableStateOf(false) }

    val currentBrushSettings = remember(brushColor, brushWidth) {
        BrushSettings(brushColor, brushWidth)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {

        SimpleToolPanel(
            currentColor = brushColor,
            currentWidth = brushWidth,
            onColorChange = { newColor -> brushColor = newColor },
            onWidthChange = { newWidth -> brushWidth = newWidth },
            onClear = {
                strokes.clear()
                currentPoints.clear()
                isDrawing = false
            },
            onUndo = {
                if (strokes.isNotEmpty()) strokes.removeLast()
            },
            hasStrokes = strokes.isNotEmpty(),
            isDrawing = isDrawing
        )

        Spacer(modifier = Modifier.height(4.dp))

        SimpleCanvas(
            strokes = strokes,
            currentPoints = currentPoints,
            currentBrushSettings = currentBrushSettings,
            isDrawing = isDrawing,
            onDrawingStateChange = { drawing -> isDrawing = drawing },
            onStrokeComplete = { newStroke -> strokes.add(newStroke) }
        )
    }
}

@Composable
fun rememberBrushSettings(context: Context): Pair<BrushSettings, (BrushSettings) -> Unit> {
    val prefs = remember {
        context.getSharedPreferences("doodle_prefs", Context.MODE_PRIVATE)
    }

    var settings by remember {
        mutableStateOf(
            BrushSettings(
                color = Color(prefs.getInt("brush_color", Color.Black.toArgb())),
                width = prefs.getFloat("brush_width", 10f)
            )
        )
    }

    val setSettings: (BrushSettings) -> Unit = { newSettings ->
        settings = newSettings
    }

    return Pair(settings, setSettings)
}

fun saveBrushSettings(context: Context, settings: BrushSettings) {
    val prefs = context.getSharedPreferences("doodle_prefs", Context.MODE_PRIVATE)
    prefs.edit {
        putInt("brush_color", settings.color.toArgb())
            .putFloat("brush_width", settings.width)
    }
}

suspend fun saveStrokesToStorage(context: Context, strokes: List<Stroke>) {
    withContext(Dispatchers.IO) {
    }
}

suspend fun loadStrokes(context: Context): List<Stroke>? {
    return withContext(Dispatchers.IO) {
        null
    }
}