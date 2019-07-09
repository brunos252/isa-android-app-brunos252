package com.infinum.shows_bruno_sacaric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val welcomeView = findViewById<TextView>(R.id.welcomeView)

        val username: String? = intent.getStringExtra("USERNAME")

        if(username != null) {
            welcomeView.text = "Welcome $username"
        }
    }
}
