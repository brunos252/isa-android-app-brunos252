package com.infinum.shows_bruno_sacaric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class EpisodesViewModel(showId: Int) : ViewModel(), Observer<List<Episode>> {

    private val episodesLiveData = MutableLiveData<List<Episode>>()
    private var showId = showId

    val liveData: LiveData<List<Episode>> get() {
        return episodesLiveData
    }

    private var episodesList = listOf<Episode>()


    init {
        episodesLiveData.value = episodesList
        EpisodesRepository.getEpisodes(showId).observeForever(this)
    }

    fun addEpisode(episode: Episode) {
        EpisodesRepository.addEpisode(episode, showId)
    }

    fun getShow() : Show {
        return ShowsRepository.getShows().value?.get(showId)!!
    }

    override fun onChanged(shows: List<Episode>?) {
        episodesList = shows ?: listOf()
        episodesLiveData.value = episodesList
    }

}