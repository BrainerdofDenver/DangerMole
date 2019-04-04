package com.example.dangermolemobile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v4.content.FileProvider
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.IOException
import java.util.*

class CameraActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var currentPhotoPath = ""
    val REQUEST_TAKE_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


        take_pic_button.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    //Code based on tutorial for initial functionality: https://www.youtube.com/watch?v=5wbeWN4hQt0
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loadPicToPreview()
    }

    //Part of Navigation Drawer
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        NavigationHandler().NavigationOnClickListener(this, this, item)
        return true
    }
    private fun dateTimeFormatter(): String{
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString() //Java is dumb, so add 1 to months
        val year = calendar.get(Calendar.YEAR).toString()
        val hour = calendar.get(Calendar.HOUR).toString()
        val min = calendar.get(Calendar.MINUTE).toString()
        val sec = calendar.get(Calendar.SECOND).toString()

        val str = hour + "_" + min + "_" + sec + "&"+ month + "_" + dayOfMonth + "_" + year
        return str
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val root = File(Environment.getExternalStorageDirectory().toString() + File.separator + "DangerMole" + File.separator)
        root.mkdirs()
        return File.createTempFile(
            dateTimeFormatter(), ".png", root
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Toast.makeText(this, "Error occurred creating file. Please try again.",
                        Toast.LENGTH_SHORT).show()
                    val folder_main = "DangerMole"

                    val f = File(Environment.getExternalStorageDirectory(), folder_main)
                    if (!f.exists()) {
                        f.mkdirs()
                    }

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.dangermolemobile.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun loadPicToPreview(){
        //https://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
        val bm = BitmapFactory.decodeFile(currentPhotoPath)
        val imgView: ImageView = findViewById(R.id.camView)

        val dimension = getSquareCropDimensionForBitmap(bm)

        val returnedBitMap = ThumbnailUtils.extractThumbnail(bm, dimension, dimension)

        imgView.setImageBitmap(returnedBitMap)
    }

    private fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }
}