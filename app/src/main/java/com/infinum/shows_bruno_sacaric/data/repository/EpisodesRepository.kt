package com.infinum.shows_bruno_sacaric.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinum.shows_bruno_sacaric.network.Api
import com.infinum.shows_bruno_sacaric.network.RetrofitClient
import com.infinum.shows_bruno_sacaric.network.models.Comment
import com.infinum.shows_bruno_sacaric.network.models.CommentResponse
import com.infinum.shows_bruno_sacaric.network.models.EpisodeCommentsResponse
import com.infinum.shows_bruno_sacaric.network.models.EpisodeResponse
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EpisodesRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
    private val episodeDetailLiveData = MutableLiveData<EpisodeResponse>()
    private val commentsLiveData = MutableLiveData<EpisodeCommentsResponse>()

    fun episodeDetailLiveData() : LiveData<EpisodeResponse> =
        episodeDetailLiveData

    fun commentsLiveData() : LiveData<EpisodeCommentsResponse> =
        commentsLiveData

    fun getEpisodeDetails(episodeId: String) {
        apiService?.getEpisode(episodeId)?.enqueue(object : Callback<EpisodeResponse> {
            override fun onFailure(call: Call<EpisodeResponse>, t: Throwable) {
                episodeDetailLiveData.value = EpisodeResponse(null, CODE_FAILED)
            }

            override fun onResponse(call: Call<EpisodeResponse>, response: Response<EpisodeResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        episodeDetailLiveData.value = EpisodeResponse(body()?.data, CODE_OK)
                        getEpisodeComments(episodeId)
                    } else {
                        episodeDetailLiveData.value = EpisodeResponse(null, CODE_NO_BODY)
                    }
                }
            }
        })
    }

    fun getEpisodeComments(episodeId: String) {
        apiService?.getEpisodeComments(episodeId)?.enqueue(object : Callback<EpisodeCommentsResponse> {
            override fun onFailure(call: Call<EpisodeCommentsResponse>, t: Throwable) {
                commentsLiveData.value = EpisodeCommentsResponse(null, CODE_FAILED)
            }

            override fun onResponse(call: Call<EpisodeCommentsResponse>, response: Response<EpisodeCommentsResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        commentsLiveData.value = EpisodeCommentsResponse(body()?.comments, CODE_OK)
                    } else {
                        commentsLiveData.value = EpisodeCommentsResponse(null, CODE_NO_BODY)
                    }
                }
            }

        })
    }

    fun postComment(comment: Comment) {
        val token = LoginRepository.tokenLiveData().value ?: return
        apiService?.postComment(token, comment)?.enqueue(object : Callback<CommentResponse> {
            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                commentsLiveData.value?.responseCode = CODE_FAILED
            }

            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        getEpisodeComments(comment.episodeId)
                    } else {
                        commentsLiveData.value?.responseCode = CODE_FAILED
                    }
                }
            }
        })
    }
}