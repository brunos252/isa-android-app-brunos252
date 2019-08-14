package com.infinum.shows_bruno_sacaric.network

import com.infinum.shows_bruno_sacaric.network.models.*
import okhttp3.RequestBody
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

    @GET("api/episodes/{episodeId}")
    fun getEpisode(@Path("episodeId") episodeId: String): Call<EpisodeResponse>

    @GET("api/episodes/{episodeId}/comments")
    fun getEpisodeComments(@Path("episodeId") episodeId: String): Call<EpisodeCommentsResponse>

    @POST("/api/shows/{showId}/like")
    fun likeShow(@Header("Authorization") token: String,
                 @Path("showId") showId: String): Call<LikeResponse>

    @POST("/api/shows/{showId}/dislike")
    fun dislikeShow(@Header("Authorization") token: String,
                    @Path("showId") showId: String): Call<LikeResponse>

    @POST("/api/episodes")
    fun postEpisode(@Header("Authorization") token: String,
                    @Body episode: EpisodeUpload): Call<EpisodeUploadResponse>

    @POST("/api/comments")
    fun postComment(@Header("Authorization") token: String,
                    @Body comment: Comment): Call<CommentResponse>

    @POST("/api/media")
    @Multipart
    fun uploadMedia(@Header("Authorization") token: String,
                    @Part("file\"; filename=\"image.jpg\"") request: RequestBody) :
            Call<MediaResponse>
}