package com.infinum.shows_bruno_sacaric.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.infinum.shows_bruno_sacaric.network.Api
import com.infinum.shows_bruno_sacaric.network.RetrofitClient
import com.infinum.shows_bruno_sacaric.network.models.EpisodesListResponse
import com.infinum.shows_bruno_sacaric.network.models.ShowDetails
import com.infinum.shows_bruno_sacaric.network.models.ShowListResponse
import com.infinum.shows_bruno_sacaric.network.models.ShowResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val FILENAME = "shows"

object ShowsRepository {

    private val apiService = RetrofitClient.retrofitInstance?.create(Api::class.java)
    private val showsListLiveData = MutableLiveData<ShowListResponse>()
    private val showDetailLiveData = MutableLiveData<ShowDetails>()

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
                    ShowListResponse(shows = null, isSuccessful = false)
            }

            override fun onResponse(call: Call<ShowListResponse>, response: Response<ShowListResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        showsListLiveData.value = ShowListResponse(
                            shows = body()?.shows,
                            isSuccessful = true
                        )
                    } else {
                        showsListLiveData.value =
                            ShowListResponse(shows = null, isSuccessful = false)
                    }
                }
            }

        })
    }

    fun getShowDetails(showId: String) {
        apiService?.getShow(showId)?.enqueue(object : Callback<ShowResponse> {
            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                showDetailLiveData.value = ShowDetails(null, null, false)
            }

            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        showDetailLiveData.value = ShowDetails(body()?.data, null, true)
                        getEpisodes(showId)
                    } else {
                        showDetailLiveData.value = ShowDetails(null, null, false)
                    }
                }
            }
        })
    }

    fun getEpisodes(showId: String) {
        apiService?.getEpisodesList(showId)?.enqueue(object : Callback<EpisodesListResponse> {
            override fun onFailure(call: Call<EpisodesListResponse>, t: Throwable) {
                showDetailLiveData.value = ShowDetails(
                    showDetailLiveData.value?.show, null, false
                )
            }

            override fun onResponse(call: Call<EpisodesListResponse>, response: Response<EpisodesListResponse>) {
                with(response) {
                    if(isSuccessful && body() != null) {
                        showDetailLiveData.value = ShowDetails(
                            showDetailLiveData.value?.show, body()?.episodes, true
                        )
                    } else {
                        showDetailLiveData.value = ShowDetails(
                            showDetailLiveData.value?.show, null, false
                        )
                    }
                }
            }

        })
    }
}