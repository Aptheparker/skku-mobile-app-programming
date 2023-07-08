package com.example.comity

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.comity.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username")
        val address = intent .getStringExtra("address")

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        Toast.makeText(this, "Welcome $username ($email)", Toast.LENGTH_LONG).show()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        // Update the username and address in the NavigationView header
        // Update the username and address in the NavigationView header
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val appbarUsernameTextView: TextView = headerView.findViewById(R.id.appbarUsernameTextView)
        appbarUsernameTextView.text = "Name: " + username
        val appbarEmailTextView: TextView = headerView.findViewById(R.id.appbarEmailTextView)
        appbarEmailTextView.text = email
        val appbarAddressTextView:TextView = headerView.findViewById(R.id.appbarAddressTextView)
        appbarAddressTextView.text = address?: "Paris"


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}