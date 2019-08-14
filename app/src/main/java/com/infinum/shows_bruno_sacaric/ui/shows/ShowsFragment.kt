package com.infinum.shows_bruno_sacaric.ui.shows

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
import com.infinum.shows_bruno_sacaric.ui.FragmentActionListener
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.fragment_shows.*
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*

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

        listener?.startLoadingDialog()

        adapter = ShowsAdapter(this)
        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.liveData.observe(this, Observer { shows ->
            with(shows) {
                if(responseCode == CODE_FAILED || responseCode == CODE_NO_BODY) {
                    listener?.closeLoadingDialog()
                    listener?.startApiErrorDialog()
                }else if(responseCode == CODE_EMPTY) {

                } else if (responseCode == CODE_OK) {
                    listener?.closeLoadingDialog()
                    adapter.setData(shows)
                    emptyView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                } else {
                    throw Exception(getString(R.string.unexpectedResponseError))
                }
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
