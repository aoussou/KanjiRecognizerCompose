package com.talisol.kanjirecognizercompose.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.applyCanvas
import com.talisol.kanjirecognizercompose.drawingUtils.dragMotionEvent
import com.talisol.kanjirecognizercompose.drawingUtils.*
import java.io.File
import kotlin.math.roundToInt

@Composable
fun KanjiRecognitionScreen(
    currentStroke: Path,
    state: DrawingState,
    onAction: (DrawingAction) -> Unit,
    kanjiRecognizer: (Bitmap) -> String,
    pathProperties: PathProperties = PathProperties(),
    strokeType: Stroke = Stroke(
        width = pathProperties.strokeWidth,
        cap = pathProperties.strokeCap,
        join = pathProperties.strokeJoin
    )
) {

    val context = LocalContext.current

    var composableBounds by remember { mutableStateOf<Rect?>(null) }
    var recognizedKanji by remember { mutableStateOf("") }

    val drawModifier = Modifier
        .aspectRatio(1f)
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White)
        .clipToBounds()
        .onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
            composableBounds = layoutCoordinates.boundsInRoot()
        }
        .dragMotionEvent(
            onDragStart = { pointerInputChange ->
                onAction(DrawingAction.UpdateMotion(MotionEvent.Down))
                onAction(DrawingAction.UpdateCurrentPosition(pointerInputChange.position))
                pointerInputChange.consumeDownChange()
            },
            onDrag = { pointerInputChange ->
                onAction(DrawingAction.UpdateMotion(MotionEvent.Move))
                onAction(DrawingAction.UpdateCurrentPosition(pointerInputChange.position))
                pointerInputChange.consumePositionChange()
            },
            onDragEnd = { pointerInputChange ->
                onAction(DrawingAction.UpdateMotion(MotionEvent.Up))
                pointerInputChange.consumeDownChange()
            }
        )
//        .border(BorderStroke(3.dp, Color.Blue))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,

    ) {

        val view = LocalView.current


        Box(
            modifier = Modifier
                .fillMaxWidth(.25F)
                .aspectRatio(1f)
                .border(BorderStroke(1.dp, Color.Blue))
                .background(Color.White),
            contentAlignment = Center

        ) {
            Text(
                recognizedKanji,
                fontSize = 72.sp,
                color = Color.Black
            )
        }



        Canvas(modifier = drawModifier) {
            when (state.motionEvent) {
                MotionEvent.Down -> {
                    onAction(DrawingAction.MovePath)
                    onAction(DrawingAction.UpdatePreviousPosition(state.currentPosition))
                }

                MotionEvent.Move -> {
                    onAction(DrawingAction.MakeBezierCurve)
                    onAction(DrawingAction.UpdatePreviousPosition(state.currentPosition))
                }

                MotionEvent.Up -> {
                    onAction(DrawingAction.MakeLine)
                    onAction(DrawingAction.AddToStrokesList(currentStroke))
                    onAction(DrawingAction.ResetCurrentPath)
                    onAction(DrawingAction.ClearUndoneStrokes)
                    onAction(DrawingAction.UpdateCurrentPosition(Offset.Unspecified))
                    // not sure why previous position required?
//                    onAction(DrawingAction.UpdatePreviousPosition(Offset.Unspecified))
//                    previousPosition = currentPosition
                    onAction(DrawingAction.UpdateMotion(MotionEvent.Idle))
                }
                else -> Unit
            }

            ///////////////////////////////////////////////////////////////////////////////
            with(drawContext.canvas.nativeCanvas) {

                val checkPoint = saveLayer(null, null)

                // This keeps the path on the canvas after you drew it
                state.allStrokes.forEach {
                    val path = it
                    drawPath(
                        color = pathProperties.color,
                        path = path,
                        style = strokeType
                    )
                }

                // This shows you the path as you are drawing
                if (state.motionEvent != MotionEvent.Idle) {
                    drawPath(
                        color = pathProperties.color,
                        path = currentStroke,
                        style = strokeType
                    )
                }
                restoreToCount(checkPoint)
            }
        }


        DrawingPropertiesMenu(

            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.3F)
                .background(Color.White)
                .border(BorderStroke(3.dp, Color.Blue))
            ,

            onUndo = {
                if (state.allStrokes.isNotEmpty()) {
                    onAction(DrawingAction.UndoLastStroke)
                    recognizedKanji = ""
                }
            },

            onRedo = {
                if (state.allUndoneStrokes.isNotEmpty()) {
                    onAction(DrawingAction.RedoLastUndoneStroke)
                }
            },

            onEraseAll = {
                if (state.allStrokes.isNotEmpty()) {
                    onAction(DrawingAction.ClearAllPaths)
                    recognizedKanji = ""
                }
            },

            onSubmit = {

                val bmp = Bitmap
                    .createBitmap(
                        (composableBounds!!.width).roundToInt(),
                        (composableBounds!!.height).roundToInt(),
                        Bitmap.Config.ARGB_8888
                    )
                    .applyCanvas {
                        translate(-composableBounds!!.left, -composableBounds!!.top)
                        view.draw(this)
                    }

                bmp.let {
                    File(context.filesDir, "screenshot.png")
                        .writeBitmap(bmp, Bitmap.CompressFormat.PNG, 85)
                }

                recognizedKanji = kanjiRecognizer(bmp)




            }

        )

    }
}


