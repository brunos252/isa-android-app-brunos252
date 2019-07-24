package com.infinum.shows_bruno_sacaric

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class EpisodesViewModel(private var showId: Int) :ViewModel(), Observer<List<Episode>> {

    private val episodesLiveData = MutableLiveData<List<Episode>>()
    private val showLiveData = MutableLiveData<Show>()


    val liveData: LiveData<List<Episode>> get() {
        return episodesLiveData
    }

    val show: LiveData<Show> get() {
        return showLiveData
    }

    init {
        episodesLiveData.value = listOf()
        showLiveData.value = ShowsRepository.getShows().value?.get(showId)
        EpisodesRepository.getEpisodes(showId).observeForever(this)
    }

    fun addEpisode(episode: Episode) {
        EpisodesRepository.addEpisode(episode, showId)
    }

    override fun onChanged(shows: List<Episode>?) {
        episodesLiveData.value = shows ?: listOf()
    }

}