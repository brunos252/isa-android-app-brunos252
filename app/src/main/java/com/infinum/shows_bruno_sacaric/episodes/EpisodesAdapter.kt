package com.infinum.shows_bruno_sacaric.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_bruno_sacaric.repository.Episode
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodesAdapter(private val clickListener: onEpisodeClicked) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    private var listOfEpisodes = listOf<Episode>()

    override fun getItemCount(): Int = listOfEpisodes.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {

        val episode = listOfEpisodes[position]

        with(holder.itemView){
            episodeTag.text = "S%02d E%02d".format(episode.season, episode.number)
            nameView?.text = episode.name
            rootView.setOnClickListener{clickListener.onClick(position)}
        }
    }

    fun setData(episodes: List<Episode>) {
        listOfEpisodes = episodes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_episode,
            parent, false))
    }

    inner class EpisodeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface onEpisodeClicked{

        fun onClick(index: Int)

    }
}