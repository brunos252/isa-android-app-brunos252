package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AddEpisodeActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)
    }
}
