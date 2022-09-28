package com.talisol.kanjirecognizercompose.viewModels

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.talisol.kanjirecognizercompose.drawingUtils.DrawingAction
import com.talisol.kanjirecognizercompose.drawingUtils.DrawingState
import com.talisol.kanjirecognizercompose.drawingUtils.MotionEvent

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DrawingVM : ViewModel() {

    private val _drawingState = MutableStateFlow(DrawingState())
    val drawingState = _drawingState.asStateFlow()

    private val _currentPath = MutableStateFlow(Path())
    val currentPath = _currentPath.asStateFlow()

    fun onAction(action: DrawingAction) {

        when (action) {
            is DrawingAction.UpdateMotion -> updateMotion(action.motionEvent)
            is DrawingAction.UpdateCurrentPosition -> updateCurrentPosition(action.position)
            is DrawingAction.UpdatePreviousPosition -> updatePreviousPosition(action.position)
            is DrawingAction.AddToStrokesList -> addToPathList(action.path)
            is DrawingAction.UndoLastStroke -> undoLastPath()
            is DrawingAction.MovePath -> moveCurrentPath()
            is DrawingAction.ResetCurrentPath -> resetCurrentPath()
            is DrawingAction.MakeBezierCurve -> makeBezierCurves()
            is DrawingAction.MakeLine -> makeLine()
            is DrawingAction.ClearAllPaths -> clearAllPaths()
            is DrawingAction.RedoLastUndoneStroke -> redoLastUndonePath()
            is DrawingAction.ClearUndoneStrokes -> clearAllUndonePaths()
        }
    }


    private fun makeBezierCurves() {
        _currentPath.value.quadraticBezierTo(
            _drawingState.value.previousPosition.x,
            _drawingState.value.previousPosition.y,
            (_drawingState.value.previousPosition.x + _drawingState.value.currentPosition.x) / 2,
            (_drawingState.value.previousPosition.y + _drawingState.value.currentPosition.y) / 2
        )
    }

    private fun makeLine() {
        _currentPath.value.lineTo(
            _drawingState.value.currentPosition.x,
            _drawingState.value.currentPosition.y
        )
    }

    private fun moveCurrentPath() {
        _currentPath.value.moveTo(
            _drawingState.value.currentPosition.x,
            _drawingState.value.currentPosition.y,
        )
    }

    private fun resetCurrentPath() {
        _currentPath.value = Path()
    }

    private fun clearAllPaths() {
        _drawingState.update { it.copy(
            allStrokes = mutableListOf(),
            allUndoneStrokes = mutableListOf()
        ) }
    }

    private fun clearAllUndonePaths() {
        _drawingState.update { it.copy(
            allUndoneStrokes = mutableListOf()
        ) }
    }

    private fun addToPathList(path: Path) {

        _drawingState.update {
            val pathsListCopy = it.allStrokes.toMutableList()
            pathsListCopy.add(path)
            it.copy(allStrokes = pathsListCopy)
        }
    }

    private fun undoLastPath() {
        _drawingState.update {
            val pathsListCopy = it.allStrokes.toMutableList()
            val undonePathsListCopy = it.allUndoneStrokes.toMutableList()

            val lastPath = pathsListCopy.removeLast()
            undonePathsListCopy.add(lastPath)
            it.copy(
                allStrokes = pathsListCopy,
                allUndoneStrokes = undonePathsListCopy
            )
        }
    }

    private fun redoLastUndonePath() {
        _drawingState.update {
            val pathsListCopy = it.allStrokes.toMutableList()
            val undonePathsListCopy = it.allUndoneStrokes.toMutableList()

            val lastUndonePath = undonePathsListCopy.removeLast()
            pathsListCopy.add(lastUndonePath)

            it.copy(
                allStrokes = pathsListCopy,
                allUndoneStrokes = undonePathsListCopy
            )
        }
    }

    private fun updateMotion(motionEvent: MotionEvent) {
        _drawingState.update { it.copy(motionEvent = motionEvent) }
    }

    private fun updateCurrentPosition(currentPosition: Offset) {
        _drawingState.update { it.copy(currentPosition = currentPosition) }
    }

    private fun updatePreviousPosition(previousPosition: Offset) {
        _drawingState.update { it.copy(previousPosition = previousPosition) }
    }

}