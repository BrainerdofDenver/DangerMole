package com.example.dangermolemobile

import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GetNearbyPlaces : AsyncTask<Pair<GoogleMap,String>,String,String>(){

    lateinit var mMap: GoogleMap
    lateinit var url: String
    lateinit var inputstream: InputStream
    lateinit var bufferedReader: BufferedReader
    lateinit var stringbuilder: StringBuilder
    @Override
    override fun doInBackground(vararg params: Pair<GoogleMap,String>): String {
        mMap = params[0].first
        url = params[0].second

        val myUrl = URL(url)
        val httpURLConnection = myUrl.openConnection()
        httpURLConnection.connect()
        inputstream = httpURLConnection.getInputStream()
        bufferedReader = BufferedReader(InputStreamReader(inputstream))
        stringbuilder = StringBuilder()
        var line = bufferedReader.readLine()

        while (line != null){
            stringbuilder.append(line)
            line = bufferedReader.readLine()
        }
        val data = stringbuilder.toString()

        return data
    }

    @Override
    override fun onPostExecute(result: String?) {
        val parentObject = JSONObject(result)
        val resultsArray = parentObject.getJSONArray("results")
        for (result in 0 until resultsArray.length()){
            val jsonObject = resultsArray.getJSONObject(result)
            val locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location")
            val latitude = locationObj.getString("lat")
            val longitude = locationObj.getString("lng")

            val name = jsonObject.getString("name")
            val latlng = LatLng(latitude.toDouble(),longitude.toDouble())

            val markerOptions = MarkerOptions()
            markerOptions.position(latlng)
            markerOptions.title(name)
            mMap.addMarker(markerOptions)
        }

    }
}