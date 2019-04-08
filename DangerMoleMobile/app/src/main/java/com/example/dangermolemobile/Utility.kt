package com.example.dangermolemobile

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast

class Utility {
    fun toastCreator(messageToDisplay: String, mContext: Context){
        Toast.makeText(mContext, messageToDisplay, Toast.LENGTH_SHORT).show()
    }

    fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }
}