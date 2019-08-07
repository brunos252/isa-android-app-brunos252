package com.infinum.shows_bruno_sacaric.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.ShowsContainerActivity


class WelcomeActivity : AppCompatActivity() {

    private val handler = Handler()

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

        handler.postDelayed({
            startActivity(ShowsContainerActivity.newInstance(this))
            finish()
        }, 1000)
    }

    override fun onStop() {
        handler.removeCallbacksAndMessages(null)
        super.onStop()
    }
}
