package com.talisol.kanjirecognizercompose.viewModels

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.talisol.kanjirecognizercompose.ml.Model
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.TransformToGrayscaleOp

class KanjiRecognitionVM(application: Application): AndroidViewModel(application) {

    private val imageProcessor = ImageProcessor.Builder()
        .add(
            ResizeOp(
                224,224, ResizeOp.ResizeMethod.BILINEAR
            )
        )
        .add(TransformToGrayscaleOp())
        .add(CastOp(DataType.FLOAT32))
        .build()

    private val jsonString = application.assets.open("preds_dict.json").bufferedReader().use { it.readText() }
    private val map = Json.parseToJsonElement(jsonString).jsonObject.toMutableMap()



    private val model = Model.newInstance(application.baseContext)

    @RequiresApi(Build.VERSION_CODES.P)
    fun predictKanji(imgBitmap: Bitmap): String {
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(imgBitmap))
        val buffer = tensorImage.tensorBuffer

        val prediction = model.process(buffer)
        val outputBuffer = prediction.outputFeature0AsTensorBuffer
        val finalOutput = outputBuffer.floatArray
        val predictedNumber = finalOutput.indexOfFirst { it == finalOutput.max() }
        val rawString = map[predictedNumber.toString()].toString()
        val processedString = rawString.replace(""""""", "")
        Log.i("TEST",processedString)
        return processedString
    }


}