package com.infinum.shows_bruno_sacaric.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_bruno_sacaric.R
import com.infinum.shows_bruno_sacaric.network.RetrofitClient
import com.infinum.shows_bruno_sacaric.network.models.ShowListItem
import com.infinum.shows_bruno_sacaric.network.models.ShowListResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_show_list.view.*

class ShowsAdapter(private val clickListener: onShowClicked) :
    RecyclerView.Adapter<ShowsAdapter.CustomViewHolder>() {

    private var listOfShows = listOf<ShowListItem>()
    var listView = false
    private val LIST_VIEW = 1
    private val GRID_VIEW = 2

    override fun getItemCount(): Int = listOfShows.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        with(holder.itemView) {
            titleView.text = listOfShows[position].name
            if (listOfShows[position].imageUrl != "") {
                Picasso.get().load(RetrofitClient.baseUrl + listOfShows[position].imageUrl)
                    .into(imageView)
            }
            if(likesCount != null) {
                likesCount.text = listOfShows[position].likesCount.toString()
            }
            rootView.setOnClickListener { clickListener.onClick(position, listOfShows[position].id) }
        }
    }

    fun setData(showsList: ShowListResponse) {
        this.listOfShows = showsList.shows!!
        notifyDataSetChanged()
    }

    fun setLayout(listView: Boolean) {
        this.listView = listView
    }

    override fun getItemViewType(position: Int): Int {
        if (listView) return LIST_VIEW
        else return GRID_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layout = if(viewType == LIST_VIEW) R.layout.item_show_list else R.layout.item_show_grid
        return CustomViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface onShowClicked{

        fun onClick(index: Int, showId: String)

    }
}