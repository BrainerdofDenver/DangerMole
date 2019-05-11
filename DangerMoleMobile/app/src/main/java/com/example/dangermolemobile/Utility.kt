package com.example.dangermolemobile

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.app.Activity
import android.support.v4.app.ActivityCompat
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * The main function of this block is a utility function for other classes to call.
 */
class Utility {
    private val REQUEST_CAM_STORAGE_PERMISSIONS_CODE = 298
    /**
     * Main function of this block is to create a toast or a pop up when clicked.
     */
    fun toastCreator(messageToDisplay: String, mContext: Context){
        Toast.makeText(mContext, messageToDisplay, Toast.LENGTH_SHORT).show()
    }

    /**
     * Main function of this block is to crop the image for a bitmap
     * @return Math.min
     */
    fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    /**
     * Main function of this block is to get permission for the camera and storage of the device.
     */
    fun requestCameraAndStoragePermissions(mActivity: Activity, mContext: Context) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mActivity,arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), REQUEST_CAM_STORAGE_PERMISSIONS_CODE )
        }

    }

    /**
     * The main function of this block is to change the floating number of the probability,
     * to a readable number.
     * @return number.toString()
     */
    fun floatSanitizer(float: Float): String{
        var number = float
        number = (number * 100)
        val dec = DecimalFormat("##.##")
        dec.roundingMode = RoundingMode.CEILING
        number = dec.format(number).toFloat()
        return number.toString()
    }

    /**
     * The main function of this block is to read the text file, using input stream,
     * into a readable string.
     */
    private fun readFileAsTextUsingInputStream(fileName: String)
            = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

    /**
     * Main function of this block is to populate the text file, when saved.
     */
    fun populateArrayFromFile(filePath: String):Array<String>{
        var str = readFileAsTextUsingInputStream(filePath)
        return str.split("\n").toTypedArray()
    }

}