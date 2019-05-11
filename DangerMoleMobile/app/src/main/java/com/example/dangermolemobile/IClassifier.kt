package com.example.dangermolemobile
// From https://github.com/KTFLITE
import android.graphics.Bitmap

/**
 * The main function of this interface is to get the results of the recognize image
 * and turn that into a single float
 * @see recognizeImage
 */
interface IClassifier {
    /**
     * Main function of this block is to just return a floating point number,
     * to the recognize image.
     */
    fun recognizeImage(bitmap: Bitmap): Float

    fun close()
}