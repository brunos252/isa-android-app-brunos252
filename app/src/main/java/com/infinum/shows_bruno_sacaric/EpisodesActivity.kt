package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_episodes.*

class EpisodesActivity : AppCompatActivity() {

    companion object {
        const val key = "SHOW"

        fun newInstance(context: Context, index: Int): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(key, index)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val show = ShowsList.listOfShows[intent.getIntExtra(key, 1)]

        showDesc.text = show.description
        supportActionBar?.title = show.name

        floatingActionButton.setOnClickListener {

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            finish()
            return true
        } else{
            return super.onOptionsItemSelected(item)
        }
    }
}
