package com.infinum.shows_bruno_sacaric.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.infinum.shows_bruno_sacaric.MyInfinumApp
import com.infinum.shows_bruno_sacaric.data.database.Likes
import com.infinum.shows_bruno_sacaric.data.database.LikesDatabase
import com.infinum.shows_bruno_sacaric.data.repository.LoginRepository.userEmail
import com.infinum.shows_bruno_sacaric.network.Api
import com.infinum.shows_bruno_sacaric.network.RetrofitClient
import com.infinum.shows_bruno_sacaric.network.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode.*

object ShowsRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
    private val showsListLiveData = MutableLiveData<ShowListResponse>()
    private val showDetailLiveData = MutableLiveData<ShowDetails>()

    private val likesDatabase: LikesDatabase = Room.databaseBuilder(
            MyInfinumApp.instance,
            LikesDatabase::class.java,
            "likes-db")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val executor = Executors.newSingleThreadExecutor()

    init {
        getShowsList()
    }

    fun showsListLiveData() : LiveData<ShowListResponse> =
        showsListLiveData

    fun showDetailLiveData() : LiveData<ShowDetails> =
        showDetailLiveData

    fun getShowsList() {
        apiService?.getShowsList()?.enqueue(object : Callback<ShowListResponse> {
            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                showsListLiveData.value =
                    ShowListResponse(shows = null, responseCode = CODE_FAILED)
            }

            override fun onResponse(call: Call<ShowListResponse>, response: Response<ShowListResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        showsListLiveData.value = ShowListResponse(
                            shows = body()?.shows,
                            responseCode = CODE_OK
                        )
                    } else {
                        showsListLiveData.value =
                            ShowListResponse(shows = null, responseCode = CODE_NO_BODY)
                    }
                }
            }

        })
    }

    fun getShowDetails(showId: String) {
        apiService?.getShow(showId)?.enqueue(object : Callback<ShowResponse> {
            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                showDetailLiveData.value = ShowDetails(null, null, null, CODE_FAILED)
            }

            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        showDetailLiveData.value = ShowDetails(body()?.data, null, null, CODE_PARTIAL)
                        getLikedStatus(showId)
                        getEpisodes(showId)
                    } else {
                        showDetailLiveData.value = ShowDetails(null, null, null, CODE_NO_BODY)
                    }
                }
            }
        })
    }

    fun getEpisodes(showId: String) {
        apiService?.getEpisodesList(showId)?.enqueue(object : Callback<EpisodesListResponse> {
            override fun onFailure(call: Call<EpisodesListResponse>, t: Throwable) {
                showDetailLiveData.value = ShowDetails(
                    showDetailLiveData.value?.show, null,
                    showDetailLiveData.value?.liked, CODE_FAILED
                )
            }

            override fun onResponse(call: Call<EpisodesListResponse>, response: Response<EpisodesListResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        val responseCode = CODE_OK
                        showDetailLiveData.value = ShowDetails(
                            showDetailLiveData.value?.show, body()?.episodes,
                            showDetailLiveData.value?.liked, responseCode
                        )
                    } else {
                        showDetailLiveData.value = ShowDetails(
                            showDetailLiveData.value?.show, null,
                            showDetailLiveData.value?.liked, CODE_NO_BODY
                        )
                    }
                }
            }
        })
    }

    fun getLikedStatus(showId: String) {
        with(likesDatabase.likesDao()) {
            val listOfLiked = getLiked(showId, userEmail)

            val liked = if(listOfLiked.isNullOrEmpty()) null else listOfLiked[0].liked

            val responseCode = if(showDetailLiveData.value?.episodes != null) CODE_OK else CODE_PARTIAL

            showDetailLiveData.value = ShowDetails(
                showDetailLiveData.value?.show, showDetailLiveData.value?.episodes,
                liked, responseCode
            )

        }
    }

    fun likeShow(showId: String) {
        val token = LoginRepository.tokenLiveData().value ?: return
        apiService?.likeShow(token, showId)?.enqueue(object: Callback<LikeResponse> {
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {}
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {}

        })
    }

    fun dislikeShow(showId: String) {
        val token = LoginRepository.tokenLiveData().value ?: return
        apiService?.dislikeShow(token, showId)?.enqueue(object: Callback<LikeResponse> {
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {}
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {}
        })
    }

    fun setLiked(showId: String, liked: Boolean) {
        with(likesDatabase.likesDao()) {
            val listOfLiked = getLiked(showId, userEmail)

            if (listOfLiked.isNullOrEmpty()) {
                insertLiked(Likes(showId = showId, userId = userEmail, liked = liked))
                updateViewLikedStatus(showId, liked)

            } else if (listOfLiked[0].liked != liked) {
                updateLiked(Likes(showId = showId, userId = userEmail, liked = liked))
                updateViewLikedStatus(showId, liked)
            }
        }
    }

    private fun updateViewLikedStatus(showId: String, liked: Boolean) {
        val respCode: ResponseCode
        with(showDetailLiveData.value!!) {
            respCode = if(show != null && episodes != null) CODE_OK else CODE_PARTIAL
        }
        val newLikesCount = if(liked) 1 else -1
        val show: Show
        with(showDetailLiveData.value?.show!!){
            show = Show(id = id, name = name, description = description, imageUrl = imageUrl,
                likesCount = (likesCount + newLikesCount), type = type)
        }
        showDetailLiveData.value = ShowDetails(
            show, showDetailLiveData.value?.episodes,
            liked, respCode
        )

        if(liked) likeShow(showId) else dislikeShow(showId)
    }
}