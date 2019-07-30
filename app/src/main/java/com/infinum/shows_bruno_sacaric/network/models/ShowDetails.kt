package com.infinum.shows_bruno_sacaric.network.models

data class ShowDetails(
    val show: Show?,
    val episodes: List<Episode>?,

    val isSuccessful: Boolean = true
)
