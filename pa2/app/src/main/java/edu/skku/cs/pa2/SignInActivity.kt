package edu.skku.cs.pa2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        // layout
        val signInButton = findViewById<Button>(R.id.Button_signin)
        val editText = findViewById<EditText>(R.id.EditText_username)

        // HTTP client
        val client = OkHttpClient()
        val url = "http://swui.skku.edu:1399/users"

        //button clicked
        signInButton.setOnClickListener{

            //Gson for post
            val gson = Gson()
            val userJson = gson.toJson(UserModel(editText.text.toString()))

            //json
            val JSON = "application/json; charset=utf-8".toMediaType()
            val requestBody = userJson.toRequestBody(JSON)

            val req = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(req).enqueue(object :  Callback{
                override fun onFailure(call: Call, e: IOException) {
                    throw Exception(e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    //fail
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")

                    //response into string
                    val str = response.body!!.string()
                    val data = gson.fromJson(str, SignInModel::class.java)

                    if(data.success){ //success
                        var intent = Intent(this@SignInActivity, MazeSelectionActivity::class.java).apply {
                            putExtra("username", editText.text.toString())
                        }
                        startActivity(intent)
                    } else { //not in the server
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(this@SignInActivity, "Wrong User Name",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

        }

    }
    }
