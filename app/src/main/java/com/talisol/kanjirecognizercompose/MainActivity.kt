package com.talisol.kanjirecognizercompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.talisol.kanjirecognizercompose.ui.screens.KanjiRecognitionScreen
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


                    KanjiRecognitionScreen(
                        currentPath,
                        drawingState,
                        drawingVM::onAction,
                        recognizerVM::predictKanji
                    )

            }
        }
    }
}

