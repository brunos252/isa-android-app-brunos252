package com.infinum.shows_bruno_sacaric.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infinum.shows_bruno_sacaric.data.database.dao.LikesDao

@Database(
    version = 1,
    entities = [Likes::class]
)
abstract class LikesDatabase : RoomDatabase() {

    abstract fun likesDao(): LikesDao
}