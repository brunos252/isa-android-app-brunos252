package com.infinum.shows_bruno_sacaric

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_episodes.*
import kotlinx.android.synthetic.main.toolbar.*

private const val EPISODE_ADDED = 109

class EpisodesActivity : AppCompatActivity(), EpisodesAdapter.onEpisodeClicked {

    companion object {
        const val SHOW_KEY = "SHOW"

        fun newInstance(context: Context, index: Int): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(SHOW_KEY, index)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)

        val index = intent.getIntExtra(SHOW_KEY, 1)

        val show = ShowsList.listOfShows[index]

        toolbar.title = show.name
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        if(show.episodes.isEmpty()){
            emptyView.visibility = View.VISIBLE
            recyclerViewEp.visibility = View.GONE

        }

        showDesc.text = show.description

        floatingActionButton.setOnClickListener {
            onAddEpisode(floatingActionButton)
        }

        recyclerViewEp.layoutManager = LinearLayoutManager(this)
        recyclerViewEp.adapter = EpisodesAdapter(show.episodes, this)

    }

    //za pritisak na pojedinacnu epizodu
    override fun onClick(index: Int) {
    }

    //ista metoda za FAB i klik na tekst
    fun onAddEpisode(view: View){
        startActivityForResult(AddEpisodeActivity.newInstance(this,
            intent.getIntExtra(SHOW_KEY, 1)), EPISODE_ADDED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == EPISODE_ADDED && resultCode == Activity.RESULT_OK){
            recyclerViewEp.adapter?.notifyDataSetChanged()
            if(ShowsList.listOfShows[intent.getIntExtra(SHOW_KEY, 1)].episodes.size == 1){
                emptyView.visibility = View.GONE
                recyclerViewEp.visibility = View.VISIBLE
            }
        }
    }
}
