package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.android.synthetic.main.toolbar.*

const val SHOW_KEY = "key"

class EpisodesFragment : Fragment(), EpisodesAdapter.onEpisodeClicked {

    companion object {
        fun newInstance(index: Int) = EpisodesFragment().apply {
            val args = Bundle()
            args.putInt(SHOW_KEY, index)
            arguments = args
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    private lateinit var viewModel: EpisodesViewModel
    private lateinit var adapter: EpisodesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(SHOW_KEY, 1)
        //val index = intent.getIntExtra(EpisodesActivity.SHOW_KEY, 1)

        adapter = EpisodesAdapter(this)
        viewModel = ViewModelProviders.of(this, MyEpisodesViewModelFactory(index!!))
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

        recyclerViewEp.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEp.adapter = adapter

        toolbar.title = show.name
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {

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
        startActivity(AddEpisodeActivity.newInstance(requireContext(), arguments?.getInt(SHOW_KEY)!!))
    }
}
