package com.infinum.shows_bruno_sacaric.episodes

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.shows_bruno_sacaric.FragmentActionListener
import com.infinum.shows_bruno_sacaric.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(SHOW_KEY, 1)

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE &&
            activity?.resources?.configuration?.screenWidthDp!! >= 600) {
            toolbar.navigationIcon = null
        } else {
            toolbar.navigationIcon = context?.getDrawable(R.drawable.ic_arrow_back_black_24dp)
        }

        viewModel = ViewModelProviders.of(activity!!).get(EpisodesViewModel::class.java)
        viewModel.selectShow(index!!)
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

        val show = viewModel.show.value!!

        recyclerViewEp.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEp.adapter = adapter

        toolbar.title = show.name
        toolbar.setNavigationOnClickListener {
            listener?.deselectShow()
            listener?.backPress()
        }

        showDesc.text = show.description

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
