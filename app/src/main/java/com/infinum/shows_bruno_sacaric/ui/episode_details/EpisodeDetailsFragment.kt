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
import com.infinum.shows_bruno_sacaric.ui.FragmentActionListener
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.network.RetrofitClient
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_episode_details.*
import kotlinx.android.synthetic.main.toolbar.*

const val EPISODE_KEY = "Episode key"

class EpisodeDetailsFragment : Fragment() {

    companion object {
        fun newInstance(episodeId: String) = EpisodeDetailsFragment().apply {
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
        return inflater.inflate(R.layout.fragment_episode_details, container, false)
    }

    private lateinit var viewModel: EpisodeDetailsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener?.startLoadingDialog()

        val episodeId = arguments?.getString(EPISODE_KEY)

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE &&
            activity?.resources?.configuration?.smallestScreenWidthDp!! >= 600) {
            toolbar.navigationIcon = null
        } else {
            toolbar.navigationIcon = context?.getDrawable(R.drawable.ic_arrow_back_gray)
        }

        viewModel = ViewModelProviders.of(activity!!).get(EpisodeDetailsViewModel::class.java)
        viewModel.selectEpisode(episodeId!!)
        viewModel.liveData.observe(this, Observer { episodeDetails ->
            if(episodeDetails.data?.id != episodeId) {
                return@Observer
            }

            listener?.closeLoadingDialog()
            with(episodeDetails) {
                if(responseCode == CODE_FAILED || responseCode == CODE_NO_BODY) {
                    listener?.closeLoadingDialog()
                    listener?.startApiErrorDialog()
                } else if(responseCode == CODE_EMPTY) {

                } else if(responseCode == CODE_OK) {
                    episodeTitle.text = data?.title
                    if(data?.seasonNum != "" && data?.episodeNum != "") {
                        episodeNumber.text = "S%d Ep%d".format(
                            data?.seasonNum?.substring(1)?.toIntOrNull(),
                            data?.episodeNum?.substring(1)?.toIntOrNull()
                        )
                    }
                    episodeDesc.text = data?.description
                    if (data?.imageUrl != "") {
                        Picasso.get().load(RetrofitClient.baseUrl + data?.imageUrl)
                            .into(episodePhoto)
                    } else {
                        episodePhoto.visibility = View.GONE
                    }
                } else {
                    throw Exception(getString(R.string.unexpectedResponseError))
                }
            }
        })

        toolbar.setNavigationOnClickListener {
            listener?.backPress()
        }

        commentsLayout.setOnClickListener {
            listener?.openCommentsClick(episodeId)
        }

    }
}
