package com.infinum.shows_bruno_sacaric.data.database

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "shows_likes",
    primaryKeys = ["showId", "userId"])
data class Likes(

    val showId: String,

    val userId: String,

    val liked: Boolean

) : Serializable