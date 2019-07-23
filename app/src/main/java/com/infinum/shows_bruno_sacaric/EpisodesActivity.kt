package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_episodes.*
import kotlinx.android.synthetic.main.toolbar.*

class EpisodesActivity : AppCompatActivity(), EpisodesAdapter.onEpisodeClicked {

    companion object {
        const val SHOW_KEY = "SHOW"

        fun newInstance(context: Context, index: Int): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(SHOW_KEY, index)
            return intent
        }
    }

    private lateinit var viewModel: EpisodesViewModel
    private var adapter = EpisodesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)

        val index = intent.getIntExtra(SHOW_KEY, 1)

        viewModel = ViewModelProviders.of(this, MyEpisodesViewModelFactory(index))
            .get(EpisodesViewModel::class.java)
        viewModel.liveData.observe(this, Observer { episodes ->
            adapter.setData(episodes)
            if(episodes.isNullOrEmpty()){
                emptyView.visibility = View.VISIBLE
                recyclerViewEp.visibility = View.GONE
            } else{
                emptyView.visibility = View.GONE
                recyclerViewEp.visibility = View.VISIBLE
            }
        })

        val show = viewModel.getShow().value!!

        recyclerViewEp.layoutManager = LinearLayoutManager(this)
        recyclerViewEp.adapter = adapter

        toolbar.title = show.name
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()
        }

        showDesc.text = show.description

        floatingActionButton.setOnClickListener {
            onAddEpisode(floatingActionButton)
        }
    }

    //za pritisak na pojedinacnu epizodu
    override fun onClick(index: Int) {
    }

    //ista metoda za FAB i klik na tekst
    fun onAddEpisode(view: View){
        startActivity(AddEpisodeActivity.newInstance(this, intent.getIntExtra(SHOW_KEY, 1)))
    }
}
