package com.infinum.shows_bruno_sacaric.ui.episodes

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
import com.infinum.shows_bruno_sacaric.ui.FragmentActionListener
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.fragment_episodes.*
import kotlinx.android.synthetic.main.fragment_episodes.emptyView
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_empty_state.*
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*

const val SHOW_KEY = "key"

class EpisodesFragment : Fragment(),
    EpisodesAdapter.onEpisodeClicked {

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
    private var likeStatus : Boolean? = null
    private var likes : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showId = arguments?.getString(SHOW_KEY)

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE &&
            activity?.resources?.configuration?.smallestScreenWidthDp!! >= 600) {
            toolbar.navigationIcon = null
        } else {
            toolbar.navigationIcon = context?.getDrawable(R.drawable.ic_arrow_back_gray)
        }

        listener?.startLoadingDialog()

        viewModel = ViewModelProviders.of(activity!!).get(EpisodesViewModel::class.java)
        viewModel.selectShow(showId!!)
        viewModel.liveData.observe(this, Observer { showDetails ->
            if(showDetails.show?.id != showId){
                return@Observer
            }
            with(showDetails){
                if(responseCode == CODE_FAILED || responseCode == CODE_NO_BODY) {
                    listener?.closeLoadingDialog()
                    listener?.startApiErrorDialog()
                }else if(responseCode == CODE_PARTIAL || responseCode == CODE_EMPTY) {

                } else if (responseCode == CODE_OK) {
                    listener?.closeLoadingDialog()
                    toolbar.title = show?.name
                    showDesc.text = show?.description
                    likeCount.visibility = View.GONE
                    likeCount.visibility = View.VISIBLE
                    likeCount.text = show?.likesCount.toString()
                    likes = show?.likesCount.toString().toInt()
                    if(!episodes.isNullOrEmpty()) {
                        adapter.setData(episodes)
                        emptyView.visibility = View.GONE
                        recyclerViewEp.visibility = View.VISIBLE
                    }
                    changeLikedState(showDetails.liked)
                } else {
                    throw Exception(getString(R.string.unexpectedResponseError))
                }
            }
        })

        recyclerViewEp.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewEp.adapter = adapter

        toolbar.setNavigationOnClickListener {
            listener?.deselectShow()
            listener?.backPress()
        }

        dislikeButton.setOnClickListener {
            if(likeStatus == true) {
                listener?.startLoadingDialog()
            }
            changeLikedState(false)
            viewModel.setLikedStatus(false)

        }

        likeButton.setOnClickListener {
            if(likeStatus == false || likeStatus == null) {
                listener?.startLoadingDialog()
                likeCount.text.reversed()
            }
            changeLikedState(true)
            viewModel.setLikedStatus(true)
        }

        addEpisodesText.setOnClickListener {
            listener?.addEpisodeClick(arguments?.getInt(SHOW_KEY)!!)
        }

        floatingActionButton.setOnClickListener {
            listener?.addEpisodeClick(arguments?.getInt(SHOW_KEY)!!)
        }
    }

    fun changeLikedState(like: Boolean?) {
        likeStatus = like
        when {
            like == null -> {
                likeButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_like_outline))
                dislikeButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_dislike_outline))
            }
            like -> {
                likeButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_like_pressed))
                dislikeButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_dislike_outline))
            }
            else -> {
                dislikeButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_dislike_pressed))
                likeButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_like_outline))
            }
        }
    }

    override fun onClick(index: Int, episodeId: String) {
        listener?.openEpisodeClick(index, episodeId)
    }
}
