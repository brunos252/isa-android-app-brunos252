package com.infinum.shows_bruno_sacaric.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.ShowsContainerActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "USERNAME"

        fun newInstance(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, username)
            return intent
        }
    }

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val username: String? = intent
            .getStringExtra(USERNAME)
            .split("@")[0]
            .capitalize()

        if(username != null) {
            welcomeView.text = getString(R.string.welcomeText, username)
        }

        handler.postDelayed({
            startActivity(ShowsContainerActivity.newInstance(this))
        }, 2000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }
}
