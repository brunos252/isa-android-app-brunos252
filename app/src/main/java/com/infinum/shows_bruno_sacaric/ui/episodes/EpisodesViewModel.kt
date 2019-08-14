package com.infinum.shows_bruno_sacaric.ui.episodes

import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.data.repository.EpisodesRepository
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode
import com.infinum.shows_bruno_sacaric.network.models.ShowDetails
import com.infinum.shows_bruno_sacaric.data.repository.ShowsRepository
import com.infinum.shows_bruno_sacaric.network.models.EpisodeUpload
import java.io.File

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

    fun addEpisode(photo: File, title: String,
                   description: String, episodeNum: String, seasonNum: String) {
        EpisodesRepository
            .addEpisode(EpisodeUpload(
                showId, "", title, description, episodeNum, seasonNum
            ), photo)
    }

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