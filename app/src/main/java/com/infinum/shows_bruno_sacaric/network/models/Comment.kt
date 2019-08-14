package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Comment(
    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String
)