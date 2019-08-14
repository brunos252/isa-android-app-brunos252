package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaResponse(
    @Json(name = "data")
    val data: MediaResponseData,

    @Transient
    var isSuccessful: Boolean = true
)

@JsonClass(generateAdapter = true)
data class MediaResponseData(
    @Json(name = "path")
    val path: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "_id")
    val id: String
)