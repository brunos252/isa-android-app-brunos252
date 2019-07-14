package com.infinum.shows_bruno_sacaric

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_episodes.*
import kotlinx.android.synthetic.main.activity_shows.*

class EpisodesActivity : AppCompatActivity(), EpisodesAdapter.onEpisodeClicked {

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

        val index = intent.getIntExtra(key, 1)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val show = ShowsList.listOfShows[index]

        if(show.episodes.isEmpty()){
            emptyView.visibility = View.VISIBLE
            recyclerViewEp.visibility = View.GONE

        }

        showDesc.text = show.description
        supportActionBar?.title = show.name

        floatingActionButton.setOnClickListener {
            onAddEpisode(floatingActionButton)
        }

        recyclerViewEp.layoutManager = LinearLayoutManager(this)
        recyclerViewEp.adapter = EpisodesAdapter(show.episodes, this)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            finish()
            return true
        } else{
            return super.onOptionsItemSelected(item)
        }
    }

    //za pritisak na pojedinacnu epizodu
    override fun onClick(index: Int) {
    }

    //ista metoda za FAB i klik na tekst
    fun onAddEpisode(view: View){
        startActivityForResult(AddEpisodeActivity.newInstance(this,
            intent.getIntExtra(key, 1)), Activity.MODE_APPEND)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Activity.MODE_APPEND && resultCode == Activity.RESULT_OK){
            recyclerViewEp.adapter?.notifyDataSetChanged()
            if(ShowsList.listOfShows[intent.getIntExtra(key, 1)].episodes.size == 1){
                emptyView.visibility = View.GONE
                recyclerViewEp.visibility = View.VISIBLE
            }
        }
    }
}
