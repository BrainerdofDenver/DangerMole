package com.example.dangermolemobile

import java.io.File

data class GalleryItem (var mImageFile: String, var mMoleData: String){

    fun galleryItem(file : String, data : String){
        mImageFile = file
        mMoleData = data
    }

    fun getmImageFile() = mImageFile
    fun getmMoleData() = mMoleData
    fun setmImageFile(newImageFile: String) {
        mImageFile = newImageFile
    }
    fun setmMoleData(newMoleData: String) {
        mMoleData = newMoleData
    }
}
