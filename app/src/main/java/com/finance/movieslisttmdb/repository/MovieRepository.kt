package com.finance.movieslisttmdb.repository

import com.custom.networksdk.Resource
import com.custom.networksdk.Response


interface MovieRepository {
    suspend fun getNowPlayingMovies(page: Int, query: Map<String, String>): Resource<Response>

    suspend fun getPopularMovies(page: Int, query: Map<String, String>): Resource<Response>

    suspend fun getGenres(): Resource<Response>

    suspend fun getMovieDetail(movieId: String): Resource<Response>

    suspend fun getMovieImages(movieId: String): Resource<Response>

    suspend fun getMovieCredits(movieId: String): Resource<Response>

    suspend fun getTrendingMovies(page: Int, query: Map<String, String>): Resource<Response>

    suspend fun discoverMovies(page: Int, options: Map<String, String>): Resource<Response>

    suspend fun searchMovies(page: Int, query: Map<String, String>): Resource<Response>

    suspend fun getPersonDetail(personId: String): Resource<Response>
}
