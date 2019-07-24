package com.infinum.shows_bruno_sacaric.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EpisodesRepository {

    private val episodesLiveData = MutableLiveData<List<Episode>>()
    private var showId = -1

    fun getEpisodes(showId: Int) : LiveData<List<Episode>> {
        if(showId != EpisodesRepository.showId) {
            episodesLiveData.value =
                ShowsRepository.getEpisodesOfShow(showId)
            EpisodesRepository.showId = showId
        }
        return episodesLiveData
    }

    fun addEpisode(episode: Episode, showId : Int) {
        ShowsRepository.addEpisodeToShow(episode, showId)
        episodesLiveData.value =
            ShowsRepository.getEpisodesOfShow(showId)
    }
}