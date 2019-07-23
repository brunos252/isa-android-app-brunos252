package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_episodes.*
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
    private lateinit var adapter: EpisodesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_episodes)

        val index = intent.getIntExtra(SHOW_KEY, 1)

        adapter = EpisodesAdapter(this)
        viewModel = ViewModelProviders.of(this, MyEpisodesViewModelFactory(index))
            .get(EpisodesViewModel::class.java)
        viewModel.liveData.observe(this, Observer { episodes ->
            if(episodes != null){
                adapter.setData(episodes)
                if(episodes.isEmpty()){
                    emptyView.visibility = View.VISIBLE
                    recyclerViewEp.visibility = View.GONE
                } else{
                    emptyView.visibility = View.GONE
                    recyclerViewEp.visibility = View.VISIBLE
                }
            } else{
                emptyView.visibility = View.VISIBLE
                recyclerViewEp.visibility = View.GONE
            }
        })

        val show = viewModel.getShow()

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
