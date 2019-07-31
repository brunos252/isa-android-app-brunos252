package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeCommentsResponse(
    @Json(name = "data")
    val episodes: List<EpisodeComment>?,

    @Transient
    var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class EpisodeComment(
    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String,

    @Json(name = "userEmail")
    val userEmail: String,

    @Json(name = "_id")
    val id: String
)
