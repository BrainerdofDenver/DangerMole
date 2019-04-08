package com.example.dangermolemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import kotlinx.android.synthetic.main.drawer_layout_camera.*
import kotlinx.android.synthetic.main.drawer_layout_localclinics.*

class NavigationHandler {
    fun NavigationOnClickListener(mContext: Context, mActivity: Activity, item: MenuItem){


        when (item.itemId) {
            R.id.nav_camera -> {

                val intent = Intent(mContext, CameraActivity::class.java)
                mActivity.finish()
                drawerLayoutCloser(mActivity)
                mContext.startActivity(intent)
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_generalinfo -> {

            }
            R.id.nav_localclinics -> {
                val intent = Intent(mContext, LocalClinicsActivity::class.java)
                mActivity.finish()
                drawerLayoutCloser(mActivity)
                mContext.startActivity(intent)
            }
            R.id.nav_aboutus -> {

            }
            R.id.nav_disclaimer -> {

            }
        }
    }

    //This function separates the activity name string from the total output of mActivity.toString()
    //mActivity.toString() outputs: com.example.dangermolemobile.CameraActivity@<instance variables>
    private fun fromActivityStringBuilder(mActivity: Activity): String{
        val fromActivityLabelArray = mActivity.toString().split('.')
        val activityString = fromActivityLabelArray[3].split('@')
        return activityString[0]
    }

    private fun drawerLayoutCloser(mActivity: Activity){
        val activityLabel = fromActivityStringBuilder(mActivity)

        when(activityLabel){
            "CameraActivity" -> {
                mActivity.drawer_layout.closeDrawer(GravityCompat.START)
            }
            "LocalClinicsActivity" -> {
                mActivity.drawer_layout_localclinics.closeDrawer(GravityCompat.START)
            }
        }
    }
}