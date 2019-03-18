package com.example.dangermolemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import java.io.File
import java.time.LocalDateTime
import java.util.*

class CameraListener {
    val APP_TAG = "DangerMole Mobile"
    val CAM_REQUEST_CODE = 0
    var photoFileName = Date(System.currentTimeMillis())
    lateinit var photoFile: File

    fun camIntentSender(requestCode: Int, mContext: Context, mActivity: Activity){
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (camIntent.resolveActivity(mContext.packageManager) != null) {
            mActivity.startActivityForResult(camIntent, requestCode)
        }
    }
}