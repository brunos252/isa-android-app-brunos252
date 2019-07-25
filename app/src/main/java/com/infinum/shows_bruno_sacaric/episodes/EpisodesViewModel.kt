package com.infinum.shows_bruno_sacaric.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.infinum.shows_bruno_sacaric.repository.Episode
import com.infinum.shows_bruno_sacaric.repository.EpisodesRepository
import com.infinum.shows_bruno_sacaric.repository.Show
import com.infinum.shows_bruno_sacaric.repository.ShowsRepository

class EpisodesViewModel :ViewModel(), Observer<List<Episode>> {

    private val episodesLiveData = MutableLiveData<List<Episode>>()
    private val showLiveData = MutableLiveData<Show>()
    private var showId = -1


    val liveData: LiveData<List<Episode>> get() {
        return episodesLiveData
    }

    val show : LiveData<Show> get() {
        return showLiveData
    }

    init {
        episodesLiveData.value = listOf()
    }

    fun selectShow(showId: Int) {
        showLiveData.value = ShowsRepository.getShows().value?.get(showId)
        EpisodesRepository.getEpisodes(showId).observeForever(this)
        this.showId = showId
    }

    fun addEpisode(episode: Episode) {
        EpisodesRepository.addEpisode(episode, showId)
    }

    override fun onChanged(shows: List<Episode>?) {
        episodesLiveData.value = shows ?: listOf()
    }

}