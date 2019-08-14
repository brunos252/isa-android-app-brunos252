package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeUploadResponse(
    @Json(name = "data")
    val data: EpisodeUploadResponseData,

    @Transient
    var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class EpisodeUploadResponseData(
    @Json(name = "showId")
    val showId: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

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
