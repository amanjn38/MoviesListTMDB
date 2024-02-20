package com.finance.movieslisttmdb.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.room.Query
import com.finance.movieslisttmdb.paging.MoviePagingSource
import com.finance.movieslisttmdb.repository.MovieRepository
import javax.inject.Inject

class Helper @Inject constructor(private val movieRepository: MovieRepository) {
    fun getNowPlayingMovies() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { MoviePagingSource(movieRepository, "nowPlaying", mapOf()) }
    ).liveData

    fun getTrendingMovies() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { MoviePagingSource(movieRepository, "trending", mapOf()) }
    ).liveData

    fun getPopularMovies() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { MoviePagingSource(movieRepository, "popular", mapOf()) }
    ).liveData

    fun getSortedMovies(query: Map<String, String>) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { MoviePagingSource(movieRepository, "sorted", query) }
    ).liveData

    fun getSearchedMovies(query: Map<String, String>) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { MoviePagingSource(movieRepository, "search", query) }
    ).liveData
}