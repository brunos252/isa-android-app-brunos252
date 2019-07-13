package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shows.*

class ShowsActivity : AppCompatActivity(), ShowsAdapter.onShowClicked {

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ShowsActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ShowsAdapter(ShowsList.listOfShows, this)
    }

    override fun onClick(index: Int) {
        startActivity(EpisodesActivity.newInstance(this, index))
    }
}
