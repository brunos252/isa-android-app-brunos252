package com.infinum.shows_bruno_sacaric.network.models

import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeCommentsResponse(
    @Json(name = "data")
    val comments: List<EpisodeComment>?,

    @Transient
    var responseCode: ResponseCode = ResponseCode.CODE_EMPTY
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
