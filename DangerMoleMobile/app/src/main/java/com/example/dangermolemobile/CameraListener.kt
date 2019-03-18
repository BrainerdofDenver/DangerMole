package com.example.dangermolemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore

class CameraListener{

    fun camIntentSender(requestCode: Int, context: Context, mActivity: Activity ) {
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (camIntent.resolveActivity(context.packageManager) != null) {
            mActivity.startActivityForResult(camIntent, requestCode)
        }
    }
}