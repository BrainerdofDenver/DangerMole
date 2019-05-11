package com.example.dangermolemobile
// From https://github.com/KTFLITE
import android.graphics.Bitmap

/**
 * The main function of this interface is to get the results of the recognize image
 * and turn that into a single float
 * @see recognizeImage
 */
interface IClassifier {

    fun recognizeImage(bitmap: Bitmap): Float

    fun close()
}