package com.example.dangermolemobile

import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.drawer_layout_localclinics.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Main function of this class is to get your location, and search up nearby clinics in your area
 */
class LocalClinicsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
                             OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var lastLocation: Location
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    /**
     * Main function of this onCreate is to get the drawer for the local clinics
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_layout_localclinics)
        setSupportActionBar(toolbar)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = getFragmentManager().findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout_localclinics, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout_localclinics.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_localclinics.setNavigationItemSelectedListener(this)
    }

    //Part of Navigation Drawer
    override fun onBackPressed() {
        if (drawer_layout_localclinics.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_localclinics.closeDrawer(GravityCompat.START)
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
    //Maps
    override fun onMapReady(map: GoogleMap){
        this.map = map
        setUpMap()
    }
    //Maps
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    /**
     * The main function of this block is to get the map set up.
     * While getting permission to get location, for the current location.
     * @see findLocalClinics
     */
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.addMarker( MarkerOptions().position(currentLatLng).title("Current Location"))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                findLocalClinics()
            }
        }

    }
    override fun onMarkerClick(p0: Marker?) = false
    /**
     * Main function of this block is to to find local clinics.
     * It uses google maps api to search for nearby places with the keyword dermatologist.
     * While calling nearby places to search for areas around it.
     * @see GetNearbyPlaces
     */
    private fun findLocalClinics(){
        val appinfo = this.packageManager.getApplicationInfo(this.packageName,PackageManager.GET_META_DATA)
        val key = appinfo.metaData.getString("com.google.android.geo.API_KEY")
        val stringBuilder = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        stringBuilder.append("&radius="+50000)
        stringBuilder.append("&location="+lastLocation.latitude.toString() + "," + lastLocation.longitude.toString())
        stringBuilder.append("&keyword="+"dermatologist")
        stringBuilder.append("&key="+key)
        val url = stringBuilder.toString()

        val dataTransfer = Pair(map,url)

        val getNearbyPlaces = GetNearbyPlaces()
        getNearbyPlaces.execute(dataTransfer)
    }
}
