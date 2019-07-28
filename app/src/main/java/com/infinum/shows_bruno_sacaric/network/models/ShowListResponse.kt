package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ShowListItem(
    @Json(name = "_id")
    val id: String,

    @Json(name = "title")
    val name: String,

    @Json(name = "imageUrl")
    val imageUrl: String,

    @Json(name = "likesCount")
    val likesCount: Int
)

@JsonClass(generateAdapter = true)
data class ShowListResponse(
    @Json(name = "data")
    val shows: List<ShowListItem>?,

    @Transient
    var isSuccessful: Boolean = true
)