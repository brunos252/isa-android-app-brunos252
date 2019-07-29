package com.infinum.shows_bruno_sacaric.repository

import java.io.Serializable

data class Episode(
    val name: String, val description: String,
    val season: Int, val number: Int
) : Serializable