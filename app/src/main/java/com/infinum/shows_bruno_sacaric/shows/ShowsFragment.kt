package com.infinum.shows_bruno_sacaric.shows

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.shows_bruno_sacaric.FragmentActionListener
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.fragment_shows.*

class ShowsFragment : Fragment(), ShowsAdapter.onShowClicked {

    private var listener: FragmentActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActionListener) {
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
            if (shows.isSuccessful) {
                if(shows.shows.isNullOrEmpty()) {
                    emptyView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    adapter.setData(shows)
                    emptyView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            } else {
                //TODO
            }
        })

        if(adapter.listView) {
             recyclerView.layoutManager = LinearLayoutManager(requireContext())
        } else {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }
        recyclerView.adapter = adapter

        logoutButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.logout_dialog_message))
                .setPositiveButton(getString(R.string.logout_dialog_positive)) { _, _ ->
                    viewModel.logout()
                    listener?.logoutStartActivity()
                }.setNegativeButton(getString(R.string.logout_dialog_negative)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

        var listView = false
        adapter.setLayout(listView)
        floatingActionButton.setOnClickListener {
            listView = !listView
            adapter.setLayout(listView)

            if (listView) {
                floatingActionButton
                    .setImageDrawable(requireContext().getDrawable(R.drawable.ic_fab_gridview_white))
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            } else {
                floatingActionButton
                    .setImageDrawable(requireContext().getDrawable(R.drawable.ic_fab_listview_white))
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }

    override fun onClick(index: Int, showId: String) {
        listener?.openShowClick(index, showId)
    }
}
