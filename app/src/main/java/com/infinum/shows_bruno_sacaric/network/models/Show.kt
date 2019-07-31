package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Show(
    @Json(name = "_id")
    val id: String,

    @Json(name = "title")
    val name: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "imageUrl")
    val imageUrl: String,

    @Json(name = "likesCount")
    val likesCount: Int,

    @Json(name = "type")
    val type: String
)

@JsonClass(generateAdapter = true)
data class ShowResponse(
    @Json(name = "data")
    val data: Show?,

    @Transient
    var isSuccessful: Boolean = true
)