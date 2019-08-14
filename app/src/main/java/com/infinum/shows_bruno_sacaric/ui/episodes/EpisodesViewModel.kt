package com.infinum.shows_bruno_sacaric.ui.episodes

import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode
import com.infinum.shows_bruno_sacaric.network.models.ShowDetails
import com.infinum.shows_bruno_sacaric.data.repository.ShowsRepository

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

    fun setLikedStatus(liked: Boolean) {
        ShowsRepository.setLiked(showId, liked)
    }

    override fun onChanged(shows: ShowDetails?) {
        showDetailsLiveData.value = shows ?: ShowDetails(
            show = null, episodes = null, liked = null, responseCode = ResponseCode.CODE_EMPTY
        )
    }

    override fun onCleared() {
        ShowsRepository.showDetailLiveData().removeObserver(this)
    }

}