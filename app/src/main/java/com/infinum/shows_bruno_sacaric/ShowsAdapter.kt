package com.infinum.shows_bruno_sacaric

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_show.view.*

class ShowsAdapter(private val listOfShows: ArrayList<Show>, private val clickListener: onShowClicked) :
    RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun getItemCount(): Int = listOfShows.size

    //svaki put kada view dode na ekran
    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {

        with(holder.itemView){
            titleView.text = listOfShows[position].name
            imageView.setImageResource(listOfShows[position].imageid)
            airDate.text = listOfShows[position].airDate
            rootView.setOnClickListener{clickListener.onClick(position)}
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false))
    }


    inner class ShowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    //da se nesto dogada dok kliknemo
    //ideja je da ga implementiramo u activityju i onda ga proslijedimo adapteru
    interface onShowClicked{

        fun onClick(index: Int)

    }
}