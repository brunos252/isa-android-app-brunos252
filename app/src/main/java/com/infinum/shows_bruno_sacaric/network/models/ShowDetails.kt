package com.infinum.shows_bruno_sacaric.network.models

data class ShowDetails(
    val show: Show?,
    val episodes: List<Episode>?,

    @Transient
    val isSuccessful: Boolean = true
)
