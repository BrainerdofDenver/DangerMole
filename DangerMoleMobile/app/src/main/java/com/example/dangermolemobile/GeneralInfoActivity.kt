package com.example.dangermolemobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.drawer_layout_generalinfo.*

/**
 * Main function is to display general information
 */
class GeneralInfoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /**
     * Main function of on create is to display general information with the id drawer_layout_generalinfo.
     * While displaying a government link
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout_generalinfo)
        setSupportActionBar(toolbar)

        val textGov: TextView = findViewById(R.id.govLink)
        textGov.setMovementMethod(LinkMovementMethod.getInstance())

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout_generalinfo, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout_generalinfo.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_generalinfo.setNavigationItemSelectedListener(this)
    }

    //Part of Navigation Drawer
    /**
     * Main function of this block is for the function of back pressing.
     */
    override fun onBackPressed() {
        if (drawer_layout_generalinfo.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_generalinfo.closeDrawer(GravityCompat.START)
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
