package com.example.dangermolemobile

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.app.Activity
import android.support.v4.app.ActivityCompat
import java.math.RoundingMode
import java.text.DecimalFormat

class Utility {
    private val REQUEST_CAM_STORAGE_PERMISSIONS_CODE = 298

    fun toastCreator(messageToDisplay: String, mContext: Context){
        Toast.makeText(mContext, messageToDisplay, Toast.LENGTH_SHORT).show()
    }

    fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    fun requestCameraAndStoragePermissions(mActivity: Activity, mContext: Context) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mActivity,arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), REQUEST_CAM_STORAGE_PERMISSIONS_CODE )
        }

    }

    fun floatSanitizer(float: Float): String{
        var number = float
        number = (number * 100)
        val dec = DecimalFormat("##.##")
        dec.roundingMode = RoundingMode.CEILING
        number = dec.format(number).toFloat()
        return number.toString()
    }

}