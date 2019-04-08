package com.example.dangermolemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import kotlinx.android.synthetic.main.drawer_layout.*

class NavigationHandler {
    fun NavigationOnClickListener(mContext: Context, mActivity: Activity, item: MenuItem){
        when (item.itemId) {
            R.id.nav_camera -> {
                val intent = Intent(mContext, CameraActivity::class.java)
                mContext.startActivity(intent)
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_generalinfo -> {
//                val intent = Intent(mContext, GeneralInformationActivity::class.java)
//                mContext.startActivity(intent)
            }
            R.id.nav_localclinics -> {


            }
            R.id.nav_aboutus -> {

            }
            R.id.nav_disclaimer -> {

            }
        }
        mActivity.drawer_layout.closeDrawer(GravityCompat.START)
    }
}