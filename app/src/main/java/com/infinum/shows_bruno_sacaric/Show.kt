package com.infinum.shows_bruno_sacaric

class Show(val id: Int, val imageid: Int,
           val name: String, val airDate: String,
           val episodes: List<Episode> = arrayListOf<Episode>(),
           val description: String = "No description available")