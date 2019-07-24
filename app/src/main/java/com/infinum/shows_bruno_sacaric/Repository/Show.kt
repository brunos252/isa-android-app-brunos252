package com.infinum.shows_bruno_sacaric.Repository

import com.infinum.shows_bruno_sacaric.Repository.Episode
import java.io.Serializable

data class Show(
    val id: Int, val imageid: Int,
    val name: String, val airDate: String,
    var episodes: ArrayList<Episode> = arrayListOf(),
    val description: String = "No description available"
) : Serializable