package com.infinum.shows_bruno_sacaric.network.models

import com.infinum.shows_bruno_sacaric.data.repository.ResponseCode

data class ShowDetails(
    val show: Show?,
    val episodes: List<Episode>?,
    val liked: Boolean?,

    val responseCode: ResponseCode = ResponseCode.CODE_EMPTY
)
