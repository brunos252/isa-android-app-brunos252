package com.infinum.shows_bruno_sacaric.network.models

import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeResponse(
    @Json(name = "data")
    val data: EpisodeResponseData?,

    @Transient
    var responseCode: ResponseCode = ResponseCode.CODE_EMPTY
)

@JsonClass(generateAdapter = true)
data class EpisodeResponseData(
    @Json(name = "description")
    val description: String,

    @Json(name = "showId")
    val showId: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "episodeNumber")
    val episodeNum: String,

    @Json(name = "season")
    val seasonNum: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "_id")
    val id: String,

    @Json(name = "imageUrl")
    val imageUrl: String
)

