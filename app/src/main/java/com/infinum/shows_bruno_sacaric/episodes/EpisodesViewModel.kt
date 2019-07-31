package com.infinum.shows_bruno_sacaric.episodes

import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.network.models.ShowDetails
import com.infinum.shows_bruno_sacaric.repository.ShowsRepository

class EpisodesViewModel :ViewModel(), Observer<ShowDetails> {

    private val showDetailsLiveData = MutableLiveData<ShowDetails>()
    private var showId = ""

    val liveData : LiveData<ShowDetails> get() {
        return showDetailsLiveData
    }

    init {
        ShowsRepository.showDetailLiveData().observeForever(this)

    }

    fun selectShow(showId: String) {
        ShowsRepository.getShowDetails(showId)
        this.showId = showId
    }
/*
    fun addEpisode(episode: Episode) {
        EpisodesRepository.addEpisode(episode, showId)
    }*/

    override fun onChanged(shows: ShowDetails?) {
        showDetailsLiveData.value = shows ?: ShowDetails(
            show = null, episodes = null, isSuccessful = false
        )
    }

    override fun onCleared() {
        ShowsRepository.showDetailLiveData().removeObserver(this)
    }

}