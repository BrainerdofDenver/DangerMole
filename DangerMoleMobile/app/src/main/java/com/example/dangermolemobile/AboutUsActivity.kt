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
import kotlinx.android.synthetic.main.drawer_layout_aboutus.*

/**
 * Gets the XML of the About Us Activity at the onCreate function
 * At R.id.isiclink and R.id.gitLink provides links to the appropriate website
 */
class AboutUsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    /**
     * Main function of this block is to make the layout for the about us.
     * Also getting links to our github and isic mole to learn more about it.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout_aboutus)
        setSupportActionBar(toolbar)

        val textIsic: TextView = findViewById(R.id.isicLink)
        textIsic.setMovementMethod(LinkMovementMethod.getInstance())
        val textGit: TextView = findViewById(R.id.gitLink)
        textGit.setMovementMethod(LinkMovementMethod.getInstance())

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout_aboutus, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout_aboutus.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_aboutus.setNavigationItemSelectedListener(this)
    }


    //Part of Navigation Drawer
    /**
     * Main function of this block is for the function of back pressing.
     */
    override fun onBackPressed() {
        if (drawer_layout_aboutus.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_aboutus.closeDrawer(GravityCompat.START)
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

