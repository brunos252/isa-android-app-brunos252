package com.infinum.shows_bruno_sacaric

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private var listener: FragmentActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentActionListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episodes, container, false)
    }

    private lateinit var viewModel: EpisodesViewModel
    private var adapter = EpisodesAdapter(this)

    private var show : Show? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(SHOW_KEY, 1)

        viewModel = ViewModelProviders.of(activity!!, MyEpisodesViewModelFactory(index!!))
            .get(EpisodesViewModel::class.java)
        viewModel.liveData.observe(this, Observer { episodes ->
            if(episodes.isNullOrEmpty()){
                emptyView.visibility = View.VISIBLE
                recyclerViewEp.visibility = View.GONE
            } else{
                adapter.setData(episodes)
                emptyView.visibility = View.GONE
                recyclerViewEp.visibility = View.VISIBLE
            }
        })

        viewModel.show.observe(this, Observer { show ->
            if(show != null){
                this.show = show
            }
        })

        //val show = viewModel.getShow().value!!

        recyclerViewEp.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEp.adapter = adapter

        toolbar.title = show?.name
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }

        showDesc.text = show?.description

        clickableText.setOnClickListener {
            listener?.addEpisodeClick(arguments?.getInt(SHOW_KEY)!!)
        }

        floatingActionButton.setOnClickListener {
            listener?.addEpisodeClick(arguments?.getInt(SHOW_KEY)!!)
        }
    }

    //za pritisak na pojedinacnu epizodu
    override fun onClick(index: Int) {
    }


}
