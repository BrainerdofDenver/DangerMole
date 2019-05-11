package com.example.dangermolemobile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.drawer_layout_gallery.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Main function is to have the usability of a gallery,
 * with functions as to display the pictures, probability, time and date.
 */
class GalleryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /**
     * Main function of this block is to create a drawer layout for the gallery.
     * Also brings up previous saved data, and is the main function of the whole
     * gallery.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout_gallery)
        setSupportActionBar(toolbar)

        val filePath = this.filesDir.toString()

        val savedDataFile = File(filePath + "/SavedData.txt")

        var listToDisplay = ArrayList<String>()
        if (savedDataFile.exists()) {
            listToDisplay = displayListCleaner(Utility().populateArrayFromFile(filePath + "/SavedData.txt"))
        }
        var arrGalleryItem: ArrayList<GalleryItem> = ArrayList()
        for (dataItem in listToDisplay){
            arrGalleryItem.add(GalleryItem(getImageFilePath(filePath,listToDisplay.indexOf(dataItem)), dataItem))
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout_gallery, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout_gallery.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_gallery.setNavigationItemSelectedListener(this)

        val adapter = GalleryArrayAdapter(this, arrGalleryItem)

        val listView: ListView = findViewById(R.id.gallery_list_view)
        listView.setAdapter(adapter)

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            /**
             * Main function of this block is to show options when the item is clicked,
             * briefly.
             */
            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {
                val intent = Intent(this@GalleryActivity, CameraActivity::class.java)
                Log.d("itemPushed: ", position.toString())
                intent.putExtra("dataLineIndex", position)
                startActivity(intent)
            }
        }
        /**
         * Main function of this block is to give options when the click is hold longer,
         * giving options to delete, or not.
         */
        listView.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                val adb = AlertDialog.Builder(this@GalleryActivity)
                adb.setTitle("Delete?")
                adb.setNegativeButton("Cancel", null)
                adb.setPositiveButton("Ok")
                { adb, which ->
                    var fileToDelete = getImageFilePath(filePath, position)
                    if (File(fileToDelete).exists()) {
                        Log.d("removeFile: ", fileToDelete)
                        File(fileToDelete).delete()
                    }
                    removeLine(savedDataFile, position)
                    //listToDisplay.removeAt(position)
                    arrGalleryItem.removeAt(position)
                    listView.invalidateViews()
                }
                adb.show()
                return true
            }
        }
    }

    private fun getImageFilePath(baseFilePath: String, position: Int): String {
        val savedDataArray = Utility().populateArrayFromFile(baseFilePath + "/SavedData.txt")
        val imageDirectory = File(Environment.getExternalStorageDirectory().toString() + "/DangerMole")
        val lastIndexOfUnderscore = savedDataArray[position].lastIndexOf("_")
        val subString = savedDataArray[position].substring(0, lastIndexOfUnderscore)
        var fileToDelete = ""
        imageDirectory.walk().forEach {
            if (it.toString().contains(subString)) {
                fileToDelete = it.absolutePath
            }
        }
        return fileToDelete
    }

    /**
     * Main function is to display the results, with formatted probability, time, and date.
     * @return formattedString
     */
    private fun listViewItemDisplaySanitizer(inputString: String): String{
        val splitStringList = inputString.split("_".toRegex())
        var formattedString = splitStringList[0] + '/' + splitStringList[1] + '/' + splitStringList[2] + " " +
                splitStringList[3] + ":" + splitStringList[4] + ":" + splitStringList[5] + "\nMole Score: " + splitStringList[6]

        return formattedString
    }

    /**
     * Main function is to to make a new list
     * @return newList brand new list to be added
     * @see listViewItemDisplaySanitizer to get the formattedString for display
     */
    private fun displayListCleaner(list: Array<String>): ArrayList<String>{
        var newList = ArrayList<String>()
        for (item in list){
            if (item != ""){

                newList.add(listViewItemDisplaySanitizer(item))
            }
        }
        return newList
    }

    // From https://stackoverflow.com/questions/19760282/android-remove-known-line-from-txt-file
    /**
     * @throws IOException
     * The main function of this block is to the file and store them into strings of an array,
     * then it would remove the indexes and write back all the line.
     */
    @Throws(IOException::class)
    fun removeLine(file: File, lineIndex: Int) {
        val lines = ArrayList<String>()
        val reader = Scanner(FileInputStream(file), "UTF-8")
        while (reader.hasNextLine())
            lines.add(reader.nextLine())
        reader.close()
        assert(lineIndex >= 0 && lineIndex <= lines.size - 1)
        Log.d("removeLine: ", lines[lineIndex])
        lines.removeAt(lineIndex)
        val writer = BufferedWriter(FileWriter(file, false))
        for (line in lines)
            writer.write(line + "\n")
        writer.flush()
        writer.close()
    }

    //Part of Navigation Drawer
    /**
     * Main function of this block is for the function of back pressing.
     */
    override fun onBackPressed() {
        if (drawer_layout_gallery.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_gallery.closeDrawer(GravityCompat.START)
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
}

