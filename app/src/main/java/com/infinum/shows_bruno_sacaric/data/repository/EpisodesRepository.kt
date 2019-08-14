package com.infinum.shows_bruno_sacaric.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinum.shows_bruno_sacaric.network.Api
import com.infinum.shows_bruno_sacaric.network.RetrofitClient
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*
import com.infinum.shows_bruno_sacaric.network.models.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object EpisodesRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
    private val episodeDetailLiveData = MutableLiveData<EpisodeResponse>()
    private val commentsLiveData = MutableLiveData<EpisodeCommentsResponse>()
    private val postEpisodeLiveData = MutableLiveData<ResponseCode>()

    fun episodeDetailLiveData() : LiveData<EpisodeResponse> =
        episodeDetailLiveData

    fun commentsLiveData() : LiveData<EpisodeCommentsResponse> =
        commentsLiveData

    fun postEpisodeLiveData() : LiveData<ResponseCode> =
        postEpisodeLiveData

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

    fun addEpisode(episode: EpisodeUpload, photo: File) {
        val token = LoginRepository.tokenLiveData().value ?: return
        apiService?.uploadMedia(token, RequestBody.create(MediaType.parse("image/jpg"), photo))
            ?.enqueue(object: Callback<MediaResponse> {
            override fun onFailure(call: Call<MediaResponse>, t: Throwable) {
                postEpisodeLiveData.value = CODE_FAILED
            }

            override fun onResponse(call: Call<MediaResponse>, response: Response<MediaResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        postEpisode(EpisodeUpload(
                            episode.showId, body()?.data?.id!!, episode.title,
                            episode.description, episode.episode, episode.season
                        ))
                    } else {
                        postEpisodeLiveData.value = CODE_NO_BODY
                    }
                }
            }

        })
    }

    fun postEpisode(episode: EpisodeUpload) {
        val token = LoginRepository.tokenLiveData().value ?: return
        apiService?.postEpisode(token, episode)?.enqueue(object : Callback<EpisodeUploadResponse> {
            override fun onFailure(call: Call<EpisodeUploadResponse>, t: Throwable) {
                postEpisodeLiveData.value = CODE_FAILED
            }

            override fun onResponse(call: Call<EpisodeUploadResponse>, response: Response<EpisodeUploadResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        ShowsRepository.getEpisodes(episode.showId)
                        postEpisodeLiveData.value = CODE_OK
                    } else {
                        postEpisodeLiveData.value = CODE_NO_BODY
                    }
                }
            }

        })
    }
}