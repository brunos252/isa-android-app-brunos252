package com.infinum.shows_bruno_sacaric.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.infinum.shows_bruno_sacaric.data.database.Likes

@Dao
interface LikesDao {

    @Query("Select * from shows_likes where showId=:showId and userId=:userId")
    fun getLiked(showId: String, userId:String) : Array<Likes>

    @Insert
    fun insertLiked(likes: Likes)

    @Update
    fun updateLiked(likes: Likes)

}