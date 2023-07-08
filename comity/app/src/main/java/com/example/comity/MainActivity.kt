package com.example.comity

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.getStartedButton)

        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf()
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.pressedButtonColor),
                ContextCompat.getColor(this, R.color.focusedButtonColor),
                ContextCompat.getColor(this, R.color.buttonColor)
            )
        )

        button.backgroundTintList = colorStateList

        val logoImageView = findViewById<ImageView>(R.id.logoImageView)

        val rotationAnimator = ObjectAnimator.ofFloat(logoImageView, "rotation", 0f, 360f)
        rotationAnimator.duration = 5000
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.interpolator = LinearInterpolator()

        rotationAnimator.start()

        button.setOnClickListener {
            val username = findViewById<EditText>(R.id.usernameEditText)
            val username2String = username.text.toString()
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("username", username2String)
            }
            startActivity(intent)
            finish()
        }
    }
}