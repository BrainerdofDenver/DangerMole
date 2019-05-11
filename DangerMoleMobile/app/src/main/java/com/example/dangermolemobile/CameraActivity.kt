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
import java.util.*
import java.util.concurrent.Executors

/**
 * The start of the function of the Camera in the application
 * Initializing variables for the photo path, request to take a photo, the folder name, and current file name
 * Also get values for tensorflow to run the app
 */
class CameraActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /**
     * Values for taking the photos, foldername and the current file name.
     * @see REQUEST_TAKE_PHOTO
     * @see folderName
     * @see currentFileName
     */
    private var currentPhotoPath = ""
    val REQUEST_TAKE_PHOTO = 1
    val folderName = "DangerMole"
    var currentFileName = ""


    //Values for tensorflow
    lateinit var classifier: Classifier
    private val executor = Executors.newSingleThreadExecutor()
    /**
     * Main function of the onCreate is to run the camera, the gallery and the tensorflow.
     * @see initTensorFlowAndLoadModel
     * @see galleryIntentHandler
     */
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
        galleryIntentHandler()

    }


    //Code based on tutorial for initial functionality: https://www.youtube.com/watch?v=5wbeWN4hQt0
    /**
     * Main function of this block is to get the result of the activity and call on
     * load picture to preview in camera.
     * @see loadPreviewFromCamera
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loadPreviewFromCamera()
    }

    //Part of Navigation Drawer
    /**
     * Main function of this block is for the function of back pressing.
     */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    //Nav
    /**
     * Main function of this block is to add an action bar if its able.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    //Nav
    /**
     * Main function of this block is to pop out an option if the item is selected.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    //Nav
    /**
     * Main function of this block is when the navigation item, if it is selected.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        NavigationHandler().navigationOnClickListener(this, this, item)
        return true
    }

    /**
     * Main function of galleryIntentHandler is to save the data to a text file
     * It would then populate a list with that saved data, with the file path going to DangerMole
     * @see loadPicFromGallerytoPreview
     * @see loadedDataToTextView saving the data with probability, time and date
     */
    private fun galleryIntentHandler(){
        val filepath = this.filesDir.toString() + "/"
        val savedDataFileName = "SavedData.txt"
        var dataIndexFromGallery = 0
        if (intent.getIntExtra("dataLineIndex", -1) != -1){
            dataIndexFromGallery = intent.getIntExtra("dataLineIndex", 0)
            val savedDataArray = Utility().populateArrayFromFile(filepath + savedDataFileName)
            val imageDirectory = File(Environment.getExternalStorageDirectory().toString() + "/DangerMole")
            val lastIndexOfUnderscore = savedDataArray[dataIndexFromGallery].lastIndexOf("_")
            val subString = savedDataArray[dataIndexFromGallery].substring(0,lastIndexOfUnderscore)
            imageDirectory.walk().forEach{
                if ( it.toString().contains(subString)) {
                    currentPhotoPath = it.absolutePath
                    currentFileName = savedDataArray[dataIndexFromGallery]
                    val textview: TextView = findViewById(R.id.probabilityView)
                    loadedDataToTextView(textview)
                }
            }
            loadPicFromGallerytoPreview()
        }
    }

    /**
     * Main function is to take a picture
     * While also need to make request on taking picture and storage
     */
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

    /**
     * Main function load a picture from gallery to preview it.
     * @see getSquareCropDimensionForBitmap
     * It crops the image first then returns with the dimension
     */
    private fun loadPicFromGallerytoPreview(){
        val bm = BitmapFactory.decodeFile(currentPhotoPath)
        val imgView: ImageView = findViewById(R.id.camView)
        val dimension = getSquareCropDimensionForBitmap(bm)
        val returnedBitMap = ThumbnailUtils.extractThumbnail(bm, dimension, dimension)
        imgView.setImageBitmap(returnedBitMap)
    }

    /**
     *Main function is to load preview of the image from the camera
     * @see getSquareCropDimensionForBitmap to crop the images
     * @see displayProbability to display the probability
     * @see addNewDataToDataFile to create a new file for the data
     */
    private fun loadPreviewFromCamera(){
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

    /**
     * Main function is to crop the bitmap using a build-in function
     */
    private fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    /**
     * Main function of display proability is to display the probability, using the id tv
     * It uses also
     * @see displayResults to get the formatted results
     * @see dateSanitizer to get the formatted date
     * @see timeSanitizer to get the formatted time
     */
    private fun displayProbability(modelData: Float, tv: TextView){
        Log.d("output to prob view", modelData.toString())
        val displayResults = Utility().floatSanitizer(modelData)
        tv.setText("Malignant Probability: " + displayResults + "%" + "\n"
                + "Date: " + dateSanitizer() +  "\n" + "Time: " + timeSanitizer())
    }

    /**
     * Main function is to get the formatted date
     * @return formattedDate
     */
    private fun dateSanitizer(): String{
        val splitStringList = currentFileName.split("_".toRegex())
        var formattedDate = splitStringList[0] + '/' + splitStringList[1] + '/' + splitStringList[2]
        return formattedDate
    }

    /**
     * Main function is to get the formatted time
     * @return formattedTime
     */
    private fun timeSanitizer(): String{
        val splitStringList = currentFileName.split("_".toRegex())
        var formattedTime = splitStringList[3].padStart(2, '0') +
                ':' + splitStringList[4].padStart(2,'0') + ":" + splitStringList[5]
        return formattedTime
    }

    /**
     * Main function is to get the formatted Probability
     * @return formattedProbability
     */
    private fun loadedProbabilitySanitizer(): String{
        val splitStringList = currentFileName.split("_".toRegex())
        val formattedProbability = Utility().floatSanitizer(splitStringList[6].toFloat())
        return formattedProbability
    }

    /**
     * Main function is to get save the probability, date and time
     * @see displayResults to get the formatted results
     * @see dateSanitizer to get the formatted date
     * @see timeSanitizer to get the formatted time
     */
    private fun loadedDataToTextView(tv: TextView){
        val formattedProbability = loadedProbabilitySanitizer()
        tv.setText("Malignant Probability: " + formattedProbability + "%" + "\n"
                + "Date: " + dateSanitizer() +  "\n" + "Time: " + timeSanitizer())
    }

    /**
     * Main function is to display, month, day of the month, year, hour, minute, seconds
     * @return str
     */
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
    //This function creates a tempfile to append random integers to the end of the file, to prevent duplicates
    /**
     * Main function of this block is to prevent duplicate files.
     * @throws IOException
     */
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

    /**
     * Main function is create new data to a new file
     * while saving it with the previous save path
     */
    private fun addNewDataToDataFile(modelData: Float){
        val filepath = this.filesDir.toString() + "/"
        val savedDataFileName = "SavedData.txt"
        initialFileChecker(File(filepath + savedDataFileName), savedDataFileName)
        openFileOutput(savedDataFileName, Context.MODE_APPEND).use {
            it.write((currentFileName + modelData.toString() + "\n").toByteArray())
            it.close()
        }
    }

    /**
     * Main function is to check if the files exists
     */
    private fun initialFileChecker(file: File, fileName: String){
        if (!(file.exists())) {
            openFileOutput(fileName, Context.MODE_APPEND).use {
                it.write("".toByteArray())
                it.close()
            }
        }
    }

    /**
     * Main function of the file is to create a root file,
     * that follows the direct path towards external storage
     */
    private fun rootFileCreator(): File
            = File(Environment.getExternalStorageDirectory().toString()
            + File.separator + folderName + File.separator)

    /**
     * Main function is to create a directory for the file to go to.
     * Checks to see if the file exist if not make a new directory
     * @return file
     */
    private fun directoryCreator(file: File): File{
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * Main function is to start the tensorflow and load in the model
     * @see Classifier
     */
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

    companion object {
        private const val MODEL_PATH = "converted_model2.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val INPUT_SIZE = 224
    }
}

