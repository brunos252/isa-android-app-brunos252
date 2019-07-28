package com.infinum.shows_bruno_sacaric.network

import com.infinum.shows_bruno_sacaric.network.models.*
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("api/users")
    fun registerUser(@Body user: User): Call<RegisterResponse>

    @POST("api/users/sessions")
    fun loginUser(@Body user: User): Call<LoginResponse>

    @GET("api/shows")
    fun getShowsList(): Call<ShowListResponse>

    @GET("api/shows/{showId}")
    fun getShow(@Path("showId") showId: String): Call<ShowResponse>

    @GET("api/shows/{showId}/episodes")
    fun getEpisodesList(@Path("showId") showId: String): Call<EpisodesListResponse>

}