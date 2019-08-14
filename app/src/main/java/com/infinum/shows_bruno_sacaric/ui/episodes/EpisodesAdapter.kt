package com.infinum.shows_bruno_sacaric.ui.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_bruno_sacaric.network.models.Episode
import com.infinum.shows_bruno_sacaric.R
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodesAdapter(private val clickListener: onEpisodeClicked) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    private var listOfEpisodes = listOf<Episode>()

    override fun getItemCount(): Int = listOfEpisodes.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {

        val episode = listOfEpisodes[position]

        with(holder.itemView){
            with(episode) {
                val s = season.toIntOrNull()
                val e = episode.episode.toIntOrNull()
                if(s == null || e == null || s > 20 || e > 99){
                    episodeTag.text = "S%02d E%02d".format(20, 99)
                } else {
                    episodeTag.text = "S%02d E%02d".format(episode.season.toInt(), episode.episode.toInt())
                }
                nameView?.text = episode.title
            }
            rootView.setOnClickListener{clickListener.onClick(position, listOfEpisodes[position].id)}
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

        fun onClick(index: Int, episodeId: String)

    }
}