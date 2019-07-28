package com.infinum.shows_bruno_sacaric.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodesListResponse(
    @Json(name = "data")
    val episodes: List<Episode>?,

    @Transient
    var isSuccessful: Boolean = true
)
