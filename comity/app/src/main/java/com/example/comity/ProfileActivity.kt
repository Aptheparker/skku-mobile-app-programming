package com.example.comity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Locale

class ProfileActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var etUsername: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageButton // Added ImageButton for back button

    data class DataModel (var location:LocationState, var current:Weather) {
        data class Weather(
            var temp_c: Double? = null,
            var wind_mph: Double? = null,
            var condition: Condition)
        data class Condition(var text: String ?= null)
        data class LocationState(var lat: Double? = null, var lon: Double? = null)
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btn = findViewById<Button>(R.id.btnCheck)
        val textView = findViewById<TextView>(R.id.tvTitle)
        val et = findViewById<EditText>(R.id.etAddress)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val client = OkHttpClient()
        val host = "https://api.weatherapi.com/v1/current.json"

        btn.setOnClickListener {
            val api_key = "ddaa761ff3484d1c98884345230805"
            val find_city = et.text
            val language = "ko" // Set the desired language to Korean
            val path = "?key=$api_key&q=$find_city&language=ko"
            val req = Request.Builder().url(host + path).build()
            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val data = response.body!!.string()
                        val dat_parse = Gson().fromJson(data, ProfileActivity.DataModel::class.java)
                        val find_lat = dat_parse.location.lat!!
                        val find_lon = dat_parse.location.lon!!

                        CoroutineScope(Dispatchers.Main).launch {
                            var concat: String = find_city.toString()
                            textView.text = concat

                            mMap.clear()
                            val marker = LatLng(find_lat, find_lon)
                            mMap.addMarker(MarkerOptions().position(marker).title("MAP"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 10F))
                        }
                    }
                }
            })
        }

        // Find views
        etUsername = findViewById(R.id.etUsername)
        etAddress = findViewById(R.id.etAddress)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack) // Find the back button view

        // Set click listener for save button
        btnSave.setOnClickListener {
            saveProfileSettings()
        }

        // Set click listener for back button
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveProfileSettings() {
        // Get user input
        val username = etUsername.text.toString().trim()
        val address = etAddress.text.toString().trim()

        // Perform validation
        if (username.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please enter username and address", Toast.LENGTH_SHORT).show()
        } else {
            // Save the profile settings
            // You can implement the logic to save the username and address to your desired storage (e.g., a database)

            // Create an intent to start the HomeActivity
            val intent = Intent(this, HomeActivity::class.java).apply {
                // Pass the username value to the intent
                putExtra("username", username)
                putExtra("address", address)
            }

            // Start the HomeActivity
            startActivity(intent)

            // Show a success message
            Toast.makeText(this, "Profile settings saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        mMap.setMapStyle(styleOptions)

        val marker = LatLng(37.295881, 126.975931)
        mMap.addMarker(MarkerOptions().position(marker).title("MAP"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,10F))
    }
}
