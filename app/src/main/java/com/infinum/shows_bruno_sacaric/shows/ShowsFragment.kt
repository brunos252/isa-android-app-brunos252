package com.infinum.shows_bruno_sacaric.shows

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_shows.*

class ShowsFragment : Fragment(), ShowsAdapter.onShowClicked {

    private var listener: FragmentActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentActionListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shows, container, false)
    }

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ShowsAdapter(this)
        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { shows ->
            if(shows != null){
                adapter.setData(shows)
            } else {
                adapter.setData(listOf())
            }
        })


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onClick(index: Int) {
        listener?.openShowClick(index)
    }
}
