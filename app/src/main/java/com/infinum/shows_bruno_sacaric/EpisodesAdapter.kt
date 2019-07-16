package com.infinum.shows_bruno_sacaric

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodesAdapter(private val listOfEpisodes: ArrayList<Episode>, private val clickListener: onEpisodeClicked) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    override fun getItemCount(): Int = listOfEpisodes.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {

        with(holder.itemView){
            nameView?.text = "${position+1}. ${listOfEpisodes[position].name}"
            rootView.setOnClickListener{clickListener.onClick(position)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode,
            parent, false))
    }

    inner class EpisodeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface onEpisodeClicked{

        fun onClick(index: Int)

    }
}