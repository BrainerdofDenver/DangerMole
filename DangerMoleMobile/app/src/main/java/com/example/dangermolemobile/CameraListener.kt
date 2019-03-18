package com.example.dangermolemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.util.*

class CameraListener {
    val APP_TAG = "DangerMole Mobile"

    lateinit var photoFile: File

    fun camIntentSender(requestCode: Int, mContext: Context, mActivity: Activity){
        var picFileName = Date(System.currentTimeMillis()).toString()
        var photoFile: File = getPicFileUri(picFileName, mContext)
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //val fileProvider = FileProvider.getUriForFile(mActivity, "com.example.dangermolemobile", photoFile) as Uri

        if (camIntent.resolveActivity(mContext.packageManager) != null) {
            mActivity.startActivityForResult(camIntent, requestCode)
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPicFileUri(fileName: String, mContext: Context): File {
        val mediaStorageDir = File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs())
        {
            Log.d(APP_TAG, "failed to create directory")
        }
        // Return the file target for the photo based on filename
        val file = File(mediaStorageDir.getPath() + File.separator + fileName)
        return file
    }
}