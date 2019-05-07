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
import kotlinx.android.synthetic.main.activity_general_info.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.drawer_layout_generalinfo.*

class GeneralInfoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout_generalinfo)
        setSupportActionBar(toolbar)

        val textGov: TextView = findViewById(R.id.govLink)
        textGov.setMovementMethod(LinkMovementMethod.getInstance())
        symptoms_link.setMovementMethod(LinkMovementMethod.getInstance())

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout_generalinfo, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout_generalinfo.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_generalinfo.setNavigationItemSelectedListener(this)
    }

    //Part of Navigation Drawer
    override fun onBackPressed() {
        if (drawer_layout_generalinfo.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_generalinfo.closeDrawer(GravityCompat.START)
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
