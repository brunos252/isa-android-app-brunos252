package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "USERNAME"

        fun newInstance(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, username)
            return intent
        }
    }

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
