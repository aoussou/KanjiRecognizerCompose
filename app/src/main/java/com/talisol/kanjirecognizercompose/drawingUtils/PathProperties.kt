package com.talisol.kanjirecognizercompose.drawingUtils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin

class PathProperties(
    var strokeWidth: Float = 10f,
    var color: Color = Color.Black,
    var strokeCap: StrokeCap = StrokeCap.Round,
    var strokeJoin: StrokeJoin = StrokeJoin.Round,
)