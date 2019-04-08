package com.example.dangermolemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import kotlinx.android.synthetic.main.drawer_layout_camera.*
import kotlinx.android.synthetic.main.drawer_layout_localclinics.*
import java.lang.NullPointerException

class NavigationHandler {
    fun NavigationOnClickListener(mContext: Context, mActivity: Activity, item: MenuItem){
        when (item.itemId) {
            R.id.nav_camera -> {
                val intent = Intent(mContext, CameraActivity::class.java)
                mActivity.finish()
                try {
                    mActivity.drawer_layout.closeDrawer(GravityCompat.START)
                }catch(e: NullPointerException){
                    mActivity.drawer_layout_localclinics.closeDrawer(GravityCompat.START)
                }
                mContext.startActivity(intent)
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_generalinfo -> {
//                val intent = Intent(mContext, GeneralInformationActivity::class.java)
//                mContext.startActivity(intent)
            }
            R.id.nav_localclinics -> {
                val intent = Intent(mContext, LocalClinicsActivity::class.java)
                mActivity.finish()
                try {
                    mActivity.drawer_layout.closeDrawer(GravityCompat.START)
                }catch(e: NullPointerException){
                    mActivity.drawer_layout_localclinics.closeDrawer(GravityCompat.START)
                }
                mContext.startActivity(intent)



            }
            R.id.nav_aboutus -> {

            }
            R.id.nav_disclaimer -> {

            }
        }
        //mActivity.drawer_layout.closeDrawer(GravityCompat.START)
    }
}