package com.finance.movieslisttmdb.api

import com.custom.networksdk.Resource
import com.custom.networksdk.Response
import com.finance.movieslisttmdb.model.Genre
import com.finance.movieslisttmdb.model.Movie
import com.finance.movieslisttmdb.model.MovieCredits
import com.finance.movieslisttmdb.model.MovieImages

interface APIService {
    suspend fun getPopularMovies(page: Int): Resource<Response>
    suspend fun getNowPlayingMovies(apiKey: String, page: Int = 1): Resource<Response>
    suspend fun getTrendingMovies(page: Int): Resource<Response>
    suspend fun getGenre(): Resource<List<Response>>
    suspend fun getMovieCredits(movieId: Int): Resource<Response>
    suspend fun getMovieImages(movieId: Int): Resource<Response>
}
