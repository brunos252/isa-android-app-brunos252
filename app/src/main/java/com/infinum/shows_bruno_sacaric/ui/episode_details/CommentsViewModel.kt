package com.infinum.shows_bruno_sacaric.ui.episode_details

import androidx.lifecycle.*
import com.infinum.shows_bruno_sacaric.network.models.Comment
import com.infinum.shows_bruno_sacaric.network.models.EpisodeCommentsResponse
import com.infinum.shows_bruno_sacaric.data.repository.EpisodesRepository
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode


class CommentsViewModel : ViewModel(), Observer<EpisodeCommentsResponse> {

    private val commentsLiveData = MutableLiveData<EpisodeCommentsResponse>()

    val liveData : LiveData<EpisodeCommentsResponse> get() {
        return commentsLiveData
    }

    init {
        EpisodesRepository.commentsLiveData().observeForever(this)

    }

    fun postComment(text: String, episodeId: String?) {
        if(text != "" && !episodeId.isNullOrEmpty())
        EpisodesRepository.postComment(Comment(text, episodeId))
    }

    override fun onChanged(shows: EpisodeCommentsResponse?) {
        commentsLiveData.value = shows ?: EpisodeCommentsResponse(
            comments = null, responseCode = ResponseCode.CODE_EMPTY
        )
    }

    override fun onCleared() {
        EpisodesRepository.commentsLiveData().removeObserver(this)
    }

}