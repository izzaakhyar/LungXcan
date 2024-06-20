package com.bangkit.lungxcan.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "modelvgg16_with_metadata.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var interpreter: Interpreter? = null

    init {
        setupInterpreter()
    }

    private fun setupInterpreter() {

        try {
            val model = FileUtil.loadMappedFile(context, modelName)
            interpreter = Interpreter(model)
        } catch (e: Exception) {
            classifierListener?.onError("Classifier initialization failed: ${e.message}")
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .add(NormalizeOp(127.0f, 128.0f))
            .build()

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

        bitmap?.copy(Bitmap.Config.ARGB_8888, true)?.let { bmp ->
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bmp))
            val inputBuffer = tensorImage.buffer

            val outputBuffer =
                TensorBuffer.createFixedSize(intArrayOf(1, 7), DataType.FLOAT32)
            interpreter?.run(inputBuffer, outputBuffer.buffer.rewind())

            val results = outputBuffer.floatArray
            val classifications = processResults(results)
            classifierListener?.onResults(classifications)
        }
    }

    private fun processResults(results: FloatArray): List<Classifications> {
        val labels = listOf(
            "CANCER",
            "COVID",
            "FIBROSIS",
            "NORMAL",
            "PLEURAL THICKENING",
            "PNEUMONIA",
            "TBC"
        )

        val categories = mutableListOf<Classifications.Category>()
        results.forEachIndexed { index, score ->
            if (score > threshold) {
                categories.add(Classifications.Category(labels[index], score))
            }
        }
        return listOf(Classifications(categories))
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<Classifications>?)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

    data class Classifications(val categories: List<Category>) {
        data class Category(val label: String, val score: Float)
    }
}