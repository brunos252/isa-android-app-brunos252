package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Episode(
    @Json(name = "_id")
    val id: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "imageUrl")
    val imageUrl: String,

    @Json(name = "episodeNumber")
    val episode: String,

    @Json(name = "season")
    val season: String
)