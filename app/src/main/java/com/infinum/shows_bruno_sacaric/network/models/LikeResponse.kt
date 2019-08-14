package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LikeResponse(
    @Json(name = "type")
    val type: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "mediaId")
    val mediaId: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "_id")
    val id: String,

    @Json(name = "likesCount")
    val likesCount: Int
)