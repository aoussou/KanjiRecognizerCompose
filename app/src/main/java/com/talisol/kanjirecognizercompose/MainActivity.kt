package com.talisol.kanjirecognizercompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.talisol.kanjirecognizercompose.screens.DrawingApp
import com.talisol.kanjirecognizercompose.ui.theme.KanjiRecognizerComposeTheme
import com.talisol.kanjirecognizercompose.viewModels.DrawingVM
import com.talisol.kanjirecognizercompose.viewModels.KanjiRecognitionVM

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KanjiRecognizerComposeTheme {

                val drawingVM = viewModel<DrawingVM>()
                val drawingState by drawingVM.drawingState.collectAsState()
                val currentPath by drawingVM.currentPath.collectAsState()

                val recognizerVM = viewModel<KanjiRecognitionVM>()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            elevation = 2.dp,
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = MaterialTheme.colors.onSurface,
                            title = {
                                Text("Kanji Recognizer")
                            },
                            actions = {}
                        )
                    }
                ) {
                    DrawingApp(
                        currentPath,
                        drawingState,
                        drawingVM::onAction,
                        recognizerVM::predictKanji
                    )
                }
            }
        }
    }
}

