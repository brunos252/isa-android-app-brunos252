package com.infinum.shows_bruno_sacaric.ui.episode_details

import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.network.models.EpisodeResponse
import com.infinum.shows_bruno_sacaric.data.repository.EpisodesRepository
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode

class EpisodeDetailsViewModel : ViewModel(), Observer<EpisodeResponse> {

    private val episodeDetailsLiveData = MutableLiveData<EpisodeResponse>()
    private var episodeId = ""

    val liveData : LiveData<EpisodeResponse> get() {
        return episodeDetailsLiveData
    }

    init {
        EpisodesRepository.episodeDetailLiveData().observeForever(this)

    }

    fun selectEpisode(episodeId: String) {
        EpisodesRepository.getEpisodeDetails(episodeId)
        this.episodeId = episodeId
    }

    override fun onChanged(episode: EpisodeResponse?) {
        episodeDetailsLiveData.value = episode ?: EpisodeResponse(
            data = null, responseCode = ResponseCode.CODE_EMPTY
        )
    }

    override fun onCleared() {
        EpisodesRepository.episodeDetailLiveData().removeObserver(this)
    }

}