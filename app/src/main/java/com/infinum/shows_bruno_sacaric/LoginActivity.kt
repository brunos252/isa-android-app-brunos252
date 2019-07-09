package com.infinum.shows_bruno_sacaric

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button = findViewById<Button>(R.id.LoginButton)
        val usernameTextView = findViewById<TextView>(R.id.usernameText)
        val passwordTextView = findViewById<TextView>(R.id.shell)


        button.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("USERNAME", usernameTextView.text.toString())
            startActivity(intent)
        }
    }
}
