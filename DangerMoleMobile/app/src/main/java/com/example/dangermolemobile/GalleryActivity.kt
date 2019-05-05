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

class GalleryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {
                val intent = Intent(this@GalleryActivity, CameraActivity::class.java)
                Log.d("itemPushed: ", position.toString())
                intent.putExtra("dataLineIndex", position)
                startActivity(intent)
            }
        }

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

    private fun listViewItemDisplaySanitizer(inputString: String): String{
        val splitStringList = inputString.split("_".toRegex())
        var formattedString = splitStringList[0] + '/' + splitStringList[1] + '/' + splitStringList[2] + " " +
                splitStringList[3] + ":" + splitStringList[4] + ":" + splitStringList[5] + "\nMole Score: " + splitStringList[6]

        return formattedString
    }

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
    override fun onBackPressed() {
        if (drawer_layout_gallery.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_gallery.closeDrawer(GravityCompat.START)
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
        NavigationHandler().navigationOnClickListener(this, this, item)
        return true
    }
}

