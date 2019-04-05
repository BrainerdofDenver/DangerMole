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
    lateinit var photoFile: File

    fun camIntentSender(requestCode: Int, mContext: Context, mActivity: Activity){
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (camIntent.resolveActivity(mContext.packageManager) != null) {
            mActivity.startActivityForResult(camIntent, requestCode)
        }
    }

    fun saveImageFile(){
        var picFileName = Date(System.currentTimeMillis()).toString()
        var filePath = Environment.getExternalStorageDirectory().toString()
        photoFile = File(filePath, picFileName + ".jpg")
    }
}