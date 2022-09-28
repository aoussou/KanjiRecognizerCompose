package com.talisol.kanjirecognizercompose.drawingUtils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

sealed class DrawingAction {

    data class UpdateMotion(val motionEvent: MotionEvent) : DrawingAction()
    data class UpdateCurrentPosition(val position: Offset): DrawingAction()
    data class UpdatePreviousPosition(val position: Offset): DrawingAction()
    data class AddToStrokesList(val path: Path): DrawingAction()
    object UndoLastStroke: DrawingAction()
    object RedoLastUndoneStroke: DrawingAction()
    object ClearUndoneStrokes: DrawingAction()
    object MovePath: DrawingAction()
    object MakeBezierCurve: DrawingAction()
    object MakeLine: DrawingAction()
    object ResetCurrentPath: DrawingAction()
    object ClearAllPaths: DrawingAction()

}
