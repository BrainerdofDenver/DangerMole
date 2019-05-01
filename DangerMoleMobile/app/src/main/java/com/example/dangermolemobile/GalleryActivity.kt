package com.example.dangermolemobile

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.drawer_layout_gallery.*
import java.io.File

class GalleryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout_gallery)
        setSupportActionBar(toolbar)

        val filePath = this.filesDir.toString()

        //var array = arrayOf("Melbourne", "Vienna", "Vancouver", "Toronto", "Calgary", "Adelaide", "Perth", "Auckland", "Helsinki", "Hamburg", "Munich", "New York", "Sydney", "Paris", "Cape Town", "Barcelona", "London", "Bangkok")
        var listToDisplay = displayListCleaner(populateArrayFromFile(filePath + "/SavedData.txt"))

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout_gallery, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout_gallery.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_gallery.setNavigationItemSelectedListener(this)

        val adapter = ArrayAdapter(this,
            R.layout.listview_item, listToDisplay)

        val listView: ListView = findViewById(R.id.gallery_list_view)
        listView.setAdapter(adapter)

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {

                // value of item that is clicked
                val itemValue = listView.getItemAtPosition(position) as String

                // Toast the values
                Toast.makeText(applicationContext,
                    "Position :$position\nItem Value : $itemValue", Toast.LENGTH_LONG)
                    .show()

                val intent = Intent(this@GalleryActivity, CameraActivity::class.java)
                intent.putExtra("formattedDataString", itemValue)
                startActivity(intent)

            }
        }

    }

    private fun listViewItemDisplaySanitizer(inputString: String): String{
        val splitStringList = inputString.split("_".toRegex())
        var formattedString = splitStringList[0] + '/' + splitStringList[1] + '/' + splitStringList[2] + " " +
                splitStringList[3] + ":" + splitStringList[4] + ":" + splitStringList[5] + " Mole Score: " + splitStringList[6]

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
        //var newList: ArrayList<String> = list.removeAt(0)
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
        NavigationHandler().NavigationOnClickListener(this, this, item)
        return true
    }

    private fun readFileAsTextUsingInputStream(fileName: String)
            = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)

    fun populateArrayFromFile(filePath: String):Array<String>{
        var str = readFileAsTextUsingInputStream(filePath)
        return str.split("\n").toTypedArray()
    }
}

