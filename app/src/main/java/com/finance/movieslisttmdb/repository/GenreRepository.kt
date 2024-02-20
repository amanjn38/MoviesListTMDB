package com.finance.movieslisttmdb.repository

import com.finance.movieslisttmdb.AppDatabase
import com.finance.movieslisttmdb.model.Genre
import com.finance.movieslisttmdb.model.MovieGenre
import com.finance.movieslisttmdb.utils.HttpUtils
import com.google.gson.Gson

class GenreRepository(
    private val movieRepository: MovieRepository,
    private val appDatabase: AppDatabase
) {
    suspend fun getGenres(): List<Genre> {
        val genresFromDb = appDatabase.genreDao().getAllGenres()

        return genresFromDb.ifEmpty {
            val genresResponse = movieRepository.getGenres()
            val responseData = HttpUtils.asString(genresResponse.data?.data)
            val gson = Gson()
            val myDataObject = gson.fromJson(responseData, MovieGenre::class.java)
            val genres = myDataObject.genres

            appDatabase.genreDao().insertAll(genres)
            genres
        }
    }
}
