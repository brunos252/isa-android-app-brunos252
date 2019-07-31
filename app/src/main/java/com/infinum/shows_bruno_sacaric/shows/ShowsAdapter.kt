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
import kotlinx.android.synthetic.main.item_show.view.*

class ShowsAdapter(private val clickListener: onShowClicked) :
    RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    private var listOfShows = listOf<ShowListItem>()

    override fun getItemCount(): Int = listOfShows.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {

        with(holder.itemView){
            titleView.text = listOfShows[position].name
            if(listOfShows[position].imageUrl != "") {
                Picasso.get().load(RetrofitClient.baseUrl + listOfShows[position].imageUrl)
                    .into(imageView)
            }
            rootView.setOnClickListener{clickListener.onClick(position, listOfShows[position].id)}
        }

    }

    fun setData(showsList: ShowListResponse) {
        this.listOfShows = showsList.shows!!
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false))
    }


    inner class ShowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface onShowClicked{

        fun onClick(index: Int, showId: String)

    }
}