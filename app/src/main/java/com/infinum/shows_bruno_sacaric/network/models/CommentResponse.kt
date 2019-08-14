package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentResponse(
    @Json(name = "data")
    val data: CommentResponseData,

    @Transient
    var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class CommentResponseData(
    @Json(name = "text")
    val text: String,

    @Json(name = "episodeId")
    val episodeId: String,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "userEmail")
    val userEmail: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "_id")
    val id: String
)