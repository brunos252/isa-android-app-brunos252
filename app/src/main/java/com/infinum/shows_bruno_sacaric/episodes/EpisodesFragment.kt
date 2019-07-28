package com.infinum.shows_bruno_sacaric.episodes

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        fun newInstance(showId: String) = EpisodesFragment().apply {
            val args = Bundle()
            args.putString(SHOW_KEY, showId)
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

        val showId = arguments?.getString(SHOW_KEY)

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE &&
            activity?.resources?.configuration?.smallestScreenWidthDp!! >= 600) {
            toolbar.navigationIcon = null
        } else {
            toolbar.navigationIcon = context?.getDrawable(R.drawable.ic_arrow_back_black_24dp)
        }

        viewModel = ViewModelProviders.of(activity!!).get(EpisodesViewModel::class.java)
        viewModel.selectShow(showId!!)
        viewModel.liveData.observe(this, Observer { showDetails ->
            if(showDetails.show?.id != showId){
                return@Observer
            }
            if(showDetails.isSuccessful){
                if(showDetails.show != null){
                    with(showDetails.show) {
                        toolbar.title = name
                        showDesc.text = description
                    }
                }
                if(showDetails.episodes.isNullOrEmpty()){
                    emptyView.visibility = View.VISIBLE
                    recyclerViewEp.visibility = View.GONE
                } else{
                    adapter.setData(showDetails.episodes)
                    emptyView.visibility = View.GONE
                    recyclerViewEp.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "failed getting details", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerViewEp.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEp.adapter = adapter

        toolbar.setNavigationOnClickListener {
            listener?.deselectShow()
            listener?.backPress()
        }

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
