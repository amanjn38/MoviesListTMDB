package com.finance.movieslisttmdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.finance.movieslisttmdb.dao.GenreDao
import com.finance.movieslisttmdb.model.Genre

@Database(entities = [Genre::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
}
