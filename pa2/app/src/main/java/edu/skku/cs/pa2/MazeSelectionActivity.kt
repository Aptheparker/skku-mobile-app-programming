package edu.skku.cs.pa2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MazeSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze_selection)

        // layout
        val userText = findViewById<TextView>(R.id.TextView_username)

        // intent: get extra string of username
        userText.text = intent.getStringExtra("username")

        //okhttp
        val client = OkHttpClient()
        val gson = Gson()
        val url = "http://swui.skku.edu:1399/maps"
        val req= Request.Builder().url(url).build()

        // Enqueue the request asynchronously
        client.newCall(req).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                throw Exception(e.toString())
            }
            override fun onResponse(call: Call, response: Response) {

                //fail
                if(!response.isSuccessful) throw IOException("Unexpected code $response")

                val str = response.body!!.string()
                val data = gson.fromJson(str, Array<MazeOverviewModel>::class.java)

                CoroutineScope(Dispatchers.Main).launch {
                    val adapter = MazeOverviewAdapter(this@MazeSelectionActivity, data)
                    val listView = findViewById<ListView>(R.id.ListView_mazelist)
                    listView.adapter = adapter
                }
            }
        })

    }
}