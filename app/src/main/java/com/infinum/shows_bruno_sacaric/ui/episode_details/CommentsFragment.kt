package com.infinum.shows_bruno_sacaric.ui.episode_details


import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.shows_bruno_sacaric.ui.FragmentActionListener
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.toolbar.*

class CommentsFragment : Fragment() {

    companion object {
        fun newInstance(episodeId: String) = CommentsFragment().apply {
            val args = Bundle()
            args.putString(EPISODE_KEY, episodeId)
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
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    private lateinit var viewModel: CommentsViewModel
    private var adapter = CommentsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.startLoadingDialog()

        val episodeId = arguments?.getString(EPISODE_KEY)

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE &&
            activity?.resources?.configuration?.smallestScreenWidthDp!! >= 600) {
            toolbar.navigationIcon = null
        } else {
            toolbar.navigationIcon = context?.getDrawable(R.drawable.ic_arrow_back_gray)
            toolbar.title = getString(R.string.Comments)
        }

        viewModel = ViewModelProviders.of(activity!!).get(CommentsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { episodeComments ->
            with(episodeComments){
                if(responseCode == CODE_FAILED || responseCode == CODE_NO_BODY) {
                    listener?.closeLoadingDialog()
                    listener?.startApiErrorDialog()
                }else if(responseCode == CODE_PARTIAL || responseCode == CODE_EMPTY) {

                } else if (responseCode == CODE_OK) {
                    listener?.closeLoadingDialog()
                    if(!comments.isNullOrEmpty()) {
                        adapter.setData(comments)
                        emptyViewComments.visibility = View.GONE
                        recyclerViewComments.visibility = View.VISIBLE
                    } else {}
                } else {
                    throw Exception(getString(R.string.unexpectedResponseError))
                }
            }
        })

        recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewComments.adapter = adapter

        toolbar.setNavigationOnClickListener {
            listener?.backPress()
        }

        postButton.setOnClickListener {
            val text = comment.text.toString()
            viewModel.postComment(text, episodeId)
            comment.text.clear()
        }
    }
}
