package com.example.dangermolemobile


import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.collections.ArrayList

/**
 * Get the appropriate output for the model probability score.
 * Get the input size of 224x224
 */
class Classifier(
    /**
     * Values for the classifier, to call on the interpreter, the pixel size and label
     * list for the tensorflow model.
     * @see interpreter
     * @see inputSize
     * @see labelList
     */
    var interpreter: Interpreter? = null,
    var inputSize: Int = 224,
    var labelList: List<String> = emptyList()
) : IClassifier {

    companion object {
        private val BATCH_SIZE = 1
        private val PIXEL_SIZE = 3
        private const val CONSTANT = 255.0f
        /**
         * Get the model and label path from Camera Activity
         * @see CameraActivity
         * @throws IOException
         * @return classifier
         */
        @Throws(IOException::class)
        fun create(assetManager: AssetManager,
                   modelPath: String,
                   labelPath: String,
                   inputSize: Int): Classifier {

            val classifier = Classifier()
            classifier.interpreter = Interpreter(classifier.loadModelFile(assetManager, modelPath))
            classifier.labelList = classifier.loadLabelList(assetManager, labelPath)
            classifier.inputSize = inputSize

            return classifier
        }
    }

    /**
     * Loads in bitmap and returns float for the model's probability
     * @param bitmap
     * @return result
     */
        // Change to recognize two things
    override fun recognizeImage(bitmap: Bitmap): kotlin.Float {
        val byteBuffer = convertBitmapToByteBuffer(bitmap)
            var result = Array(1){FloatArray(labelList.size)}
        interpreter!!.run(byteBuffer, result)
            Log.d("predict size: ", result.size.toString())
            Log.d("predict indices: ", result.indices.toString())
            Log.d("labellist size: ", labelList.size.toString())
            Log.d("value at index 0: ", result[0][0].toString())

        return result[0][0]
    }

    /**
     *An overrride function that closes the interpreter
     *
     */
    override fun close() {
        interpreter!!.close()
        interpreter = null
    }

    /**
     * loads the model file, and return readable values for the model
     * @return fileChannel.map
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Loads in the label list for the model to read off of.
     * @throws IOException
     * labelList which just returns 1 string in the label list
     * @return labelList
     */
    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        val labelList:ArrayList<String> = ArrayList()

        //return labelList
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        while (true) {
            val line = reader.readLine() ?: break
            Log.d("list reading:", line.toString())
            labelList.add(line)
        }
        reader.close()
        return labelList
    }

    /**
     * Get the bitmap and transform it into byte buffer for the model
     * @return byteBuffer
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(BATCH_SIZE * inputSize * inputSize * PIXEL_SIZE * 4)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val value = intValues[pixel++]
                byteBuffer.putFloat((value shr 16 and 0xFF).toFloat()/ CONSTANT)
                byteBuffer.putFloat((value shr 8 and 0xFF).toFloat() / CONSTANT)
                byteBuffer.putFloat((value and 0xFF).toFloat() / CONSTANT)
            }
        }
        return byteBuffer
    }


}