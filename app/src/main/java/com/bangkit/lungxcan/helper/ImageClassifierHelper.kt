package com.bangkit.lungxcan.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.bangkit.lungxcan.R
//import androidx.media.ImageDecoder
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
//import org.tensorflow.lite.support.image.ops.CastOp
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.metadata.schema.NormalizationOptions
import org.tensorflow.lite.support.metadata.schema.NormalizationOptionsT
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
//import org.tensorflow.lite.task.vision.classifier.ImageClassifierOptions
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "modelvgg16_with_metadata.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var interpreter: Interpreter? = null
    private var imageClassifier: ImageClassifier? = null

    init {
        setupInterpreter()
    }

    private fun setupInterpreter() {

//        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
//            .setScoreThreshold(threshold)
//            .setMaxResults(maxResults)
//        val baseOptionsBuilder = BaseOptions.builder()
//            .setNumThreads(4)
//        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            val model = FileUtil.loadMappedFile(context, modelName)
            interpreter = Interpreter(model)

//            imageClassifier = ImageClassifier.createFromFileAndOptions(
//                context, modelName, optionsBuilder.build()
//            )
        } catch (e: Exception) {
            classifierListener?.onError("Classifier initialization failed: ${e.message}")
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
//        if (imageClassifier == null) {
//            setupInterpreter()
//        }
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(org.tensorflow.lite.DataType.FLOAT32))
            .add(NormalizeOp(127.0f, 128.0f)) // Normalizing to [0, 1]
            .build()

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

        bitmap?.copy(Bitmap.Config.ARGB_8888, true)?.let { bmp ->
            val grayscaleBitmap = convertToGrayscale(bmp)
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(grayscaleBitmap))
            val inputBuffer = tensorImage.buffer

            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 7), org.tensorflow.lite.DataType.FLOAT32)
            interpreter?.run(inputBuffer, outputBuffer.buffer.rewind())

            val results = outputBuffer.floatArray
            //val hasils = imageClassifier?.classify(tensorImage)
            val classifications = processResults(results)
            classifierListener?.onResults(classifications)
        }
    }

    private fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorMatrixFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayscaleBitmap
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

//class ImageClassifierHelper(
//    var threshold: Float = 0.1f,
//    var maxResults: Int = 3,
//    val modelName: String = "lung_disease.tflite",
//    val context: Context,
//    val classifierListener: ClassifierListener?
//) {
//    private var imageClassifier: ImageClassifier? = null
//
//    private fun setupImageClassifier() {
//        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
//        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
//            .setScoreThreshold(threshold)
//            .setMaxResults(maxResults)
//        val baseOptionsBuilder = BaseOptions.builder()
//            .setNumThreads(4)
//        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())
//
//        try {
//            imageClassifier = ImageClassifier.createFromFileAndOptions(
//                context, modelName, optionsBuilder.build()
//            )
//        } catch (e: IllegalStateException) {
//            classifierListener?.onError(context.getString(R.string.classifier_failed))
//            Log.e(TAG, e.message.toString())
//        }
//    }
//
//    fun classifyStaticImage(imageUri: Uri) {
//        // TODO: mengklasifikasikan imageUri dari gambar statis.
//        if (imageClassifier == null) {
//            setupImageClassifier()
//        }
//        val imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
//            .add(CastOp(DataType.FLOAT32))
//            .build()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
//            ImageDecoder.decodeBitmap(source)
//        } else {
//            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
//            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
//            val results = imageClassifier?.classify(tensorImage)
//            classifierListener?.onResults(
//                results
//            )
//        }
//
//    }
//
//    interface ClassifierListener {
//        fun onError(error: String)
//        fun onResults(
//            results: List<Classifications>?,
//        )
//    }
//
//    companion object {
//        private const val TAG = "ImageClassifierHelper"
//    }

//}