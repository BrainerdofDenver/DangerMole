package com.example.dangermolemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.view.GravityCompat
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.drawer_layout_aboutus.*
import kotlinx.android.synthetic.main.drawer_layout_camera.*
import kotlinx.android.synthetic.main.drawer_layout_gallery.*
import kotlinx.android.synthetic.main.drawer_layout_generalinfo.*
import kotlinx.android.synthetic.main.drawer_layout_localclinics.*

class NavigationHandler {
    fun navigationOnClickListener(mContext: Context, mActivity: Activity, item: MenuItem){

        when (item.itemId) {
            R.id.nav_camera -> {
                val intent = Intent(mContext, CameraActivity::class.java)
                navItemHandler(mActivity, mContext, intent)
            }
            R.id.nav_gallery -> {
                val intent = Intent(mContext, GalleryActivity::class.java)
                navItemHandler(mActivity, mContext, intent)
            }
            R.id.nav_generalinfo -> {
                val intent = Intent(mContext, GeneralInfoActivity::class.java)
                navItemHandler(mActivity, mContext, intent)
            }
            R.id.nav_localclinics -> {
                val intent = Intent(mContext, LocalClinicsActivity::class.java)
                navItemHandler(mActivity, mContext, intent)
            }
            R.id.nav_aboutus -> {
                val intent = Intent(mContext, AboutUsActivity::class.java)
                navItemHandler(mActivity, mContext, intent)
            }
            R.id.nav_disclaimer -> {
                disclaimerToastCreator(mContext, mActivity)
            }
        }
    }

    private fun navItemHandler(mActivity: Activity, mContext: Context, intent: Intent){
        mActivity.finish()
        mContext.startActivity(intent)
        drawerLayoutCloser(mActivity)
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
            "GeneralInfoActivity" -> {
                mActivity.drawer_layout_generalinfo.closeDrawer(GravityCompat.START)
            }
            "AboutUsActivity" -> {
                mActivity.drawer_layout_aboutus.closeDrawer(GravityCompat.START)
            }
            "GalleryActivity" -> {
                mActivity.drawer_layout_gallery.closeDrawer(GravityCompat.START)
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

    private fun disclaimerToastCreator(mContext: Context, mActivity: Activity){
        val toast = Toast.makeText(mContext, mContext.getString(R.string.full_disclaimer), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)

        val inflater = mActivity.getLayoutInflater()
        val layout = inflater.inflate(R.layout.toast,
            mActivity.findViewById<ViewGroup>(R.id.toast_layout_root)
        )

        val text = layout.findViewById(R.id.text) as TextView
        text.setText(mContext.getString(R.string.full_disclaimer))
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.setView(layout)
        toast.show()
    }

}