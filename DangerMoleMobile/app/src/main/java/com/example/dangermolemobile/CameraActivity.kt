package com.example.dangermolemobile

import android.content.Context
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
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.drawer_layout_camera.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var currentPhotoPath = ""
    val REQUEST_TAKE_PHOTO = 1
    val folderName = "DangerMole"
    var currentFileName = ""


    //Values for tensorflow
    lateinit var classifier: Classifier
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout_camera)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_camera.setNavigationItemSelectedListener(this)

        Utility().requestCameraAndStoragePermissions(this, this)

        take_pic_button.setOnClickListener {
            dispatchTakePictureIntent()
        }

        initTensorFlowAndLoadModel()
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
    //Nav
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    //Nav
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    //Nav
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        NavigationHandler().NavigationOnClickListener(this, this, item)
        return true
    }

    private fun dispatchTakePictureIntent() {
        Utility().requestCameraAndStoragePermissions(this, this)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Thread.sleep(1000)
                    Utility().toastCreator(getString(R.string.file_creation_error_msg), this)
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
        //Get text for probablity view up
        val probTextView: TextView = findViewById(R.id.probabilityView)
        val dimension = getSquareCropDimensionForBitmap(bm)
        var bitmap = Bitmap.createScaledBitmap(bm, INPUT_SIZE, INPUT_SIZE, false)
        //Call on the classifier to get bitmap of the image
        var results = classifier.recognizeImage(bitmap)
        val returnedBitMap = ThumbnailUtils.extractThumbnail(bm, dimension, dimension)
        displayProbability(results, probTextView)
        addNewDataToDataFile(results)
        imgView.setImageBitmap(returnedBitMap)
    }

    private fun displayProbability(modelData: Float, tv: TextView){
        Log.d("output to prob view", modelData.toString())
        val displayResults = floatSanitizer(modelData)
        tv.setText("Malignant Probability: " + displayResults + "%" + "\n"
                + "Date: " + dateSanitizer() +  "\n" + "Time: " + timeSanitizer())
    }

    private fun floatSanitizer(float: Float): String{
        var number = float
        number = (number * 100)
        val dec = DecimalFormat("##.##")
        dec.roundingMode = RoundingMode.CEILING
        number = dec.format(number).toFloat()
        return number.toString()
    }

    private fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    private fun rootFileCreator(): File
            = File(Environment.getExternalStorageDirectory().toString()
            + File.separator + folderName + File.separator)

    private fun directoryCreator(file: File): File{
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    companion object {
        private const val MODEL_PATH = "converted_model2.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val INPUT_SIZE = 224
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = Classifier.create(
                    assets,
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE)

            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    private fun fileNameCreator(): String{
        val calendar = Calendar.getInstance()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString() //Java is dumb, so add 1 to months
        val year = calendar.get(Calendar.YEAR).toString()
        val hour = calendar.get(Calendar.HOUR).toString()
        val min = calendar.get(Calendar.MINUTE).toString()
        val sec = calendar.get(Calendar.SECOND).toString()

        val str = month + "_" + dayOfMonth + "_" + year +  "_" + hour + "_" + min + "_" + sec +  "_"
        currentFileName = str
        return str
    }

    private fun dateSanitizer(): String{
        //var currentDateTime = currentFileName
        val splitStringList = currentFileName.split("_".toRegex())
        var formattedDate = splitStringList[0] + '/' + splitStringList[1] + '/' + splitStringList[2]
        return formattedDate
    }

    private fun timeSanitizer(): String{
        val splitStringList = currentFileName.split("_".toRegex())
        var formattedTime = splitStringList[3].padStart(2, '0') +
                ':' + splitStringList[4].padStart(2,'0') + ":" + splitStringList[5]
        return formattedTime
    }

    //This function creates a tempfile to append random integers to the end of the file, to prevent duplicates
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val fileRoot = rootFileCreator()
        directoryCreator(fileRoot)
        return File.createTempFile(
            fileNameCreator(), ".png", fileRoot
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun addNewDataToDataFile(modelData: Float){
        val filepath = this.filesDir.toString() + "/"
        val savedDataFileName = "SavedData.txt"
        initialFileChecker(File(filepath + savedDataFileName), savedDataFileName)
        openFileOutput(savedDataFileName, Context.MODE_APPEND).use {
            it.write((currentFileName + modelData.toString() + "\n").toByteArray())
            it.close()
        }
    }

    private fun initialFileChecker(file: File, fileName: String){
        if (!(file.exists())) {
            openFileOutput(fileName, Context.MODE_APPEND).use {
                it.write("".toByteArray())
                it.close()
            }
        }
    }
}