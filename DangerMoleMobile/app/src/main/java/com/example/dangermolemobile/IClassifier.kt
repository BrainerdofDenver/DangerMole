package com.example.dangermolemobile
// From https://github.com/KTFLITE
import android.graphics.Bitmap


interface IClassifier {

    fun recognizeImage(bitmap: Bitmap): Float

    fun close()
}