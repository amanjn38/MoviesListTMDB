package com.finance.movieslisttmdb.repository

import com.custom.networksdk.RequestViewModel
import com.custom.networksdk.Resource
import com.custom.networksdk.Response
import com.finance.movieslisttmdb.utils.Constants.Companion.API_KEY
import com.finance.movieslisttmdb.utils.Constants.Companion.AUTHORIZATION_CODE
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val requestViewModel: RequestViewModel) :
    MovieRepository {

    override suspend fun getNowPlayingMovies(
        page: Int,
        query: Map<String, String>
    ): Resource<Response> =
        executeRequestWithPagination("movie/now_playing", page, query)

    override suspend fun getPopularMovies(
        page: Int,
        query: Map<String, String>
    ): Resource<Response> =
        executeRequestWithPagination("movie/popular", page, query)

    override suspend fun getGenres(): Resource<Response> =
        executeRequest("genre/movie/list")

    override suspend fun getMovieDetail(movieId: String): Resource<Response> =
        executeRequest("movie/$movieId")

    override suspend fun getMovieImages(movieId: String): Resource<Response> =
        executeRequest("movie/$movieId/images")

    override suspend fun getMovieCredits(movieId: String): Resource<Response> =
        executeRequest("movie/$movieId/credits")

    override suspend fun getTrendingMovies(
        page: Int,
        query: Map<String, String>
    ): Resource<Response> =
        executeRequestWithPagination("trending/movie/day", page, query)

    override suspend fun discoverMovies(
        page: Int,
        options: Map<String, String>
    ): Resource<Response> {
        return executeRequestForSearchWithPagination("discover/movie", options, page)
    }

    override suspend fun searchMovies(
        page: Int,
        query: Map<String, String>
    ): Resource<Response> {
        return executeRequestForSearchWithPagination("search/movie", query, page)

    }

    private suspend fun executeRequestWithPagination(
        endpoint: String,
        page: Int,
        queryMap: Map<String, String>?
    ): Resource<Response> {
        val url = "https://api.themoviedb.org/3"
        val headers = createQueryMap(page)
        return executeRequestWithPagination(url, endpoint, queryMap, headers, page)
    }

    private suspend fun executeRequestForSearchWithPagination(
        endpoint: String,
        queryMap: Map<String, String>,
        page: Int
    ): Resource<Response> {
        val url = "https://api.themoviedb.org/3"
        val headers = createQueryMap()
        return executeRequestWithPagination(url, endpoint, queryMap, headers, page)
    }

    private suspend fun executeRequest(
        endpoint: String
    ): Resource<Response> {
        val url = "https://api.themoviedb.org/3"
        val headers = createQueryMap()
        return executeRequest(url, endpoint, mapOf(), headers)
    }

    private suspend fun executeRequest(
        url: String,
        endpoint: String,
        queryMap: Map<String, String>? = null,
        headers: Map<String, String>
    ): Resource<Response> {
        return try {
            val result =
                requestViewModel.executeRequest(url, endpoint, "GET", queryMap, headers, null)
            result
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.message}", null)
        }
    }

    private suspend fun executeRequestWithPagination(
        url: String,
        endpoint: String,
        queryMap: Map<String, String>? = null,
        headers: Map<String, String>,
        page: Int
    ): Resource<Response> {
        return try {
            val result = requestViewModel.executeRequest(
                url,
                endpoint,
                "GET",
                queryMap,
                headers,
                null,
                page
            )
            result
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.message}", null)
        }
    }

    private fun createQueryMap(page: Int = 1): Map<String, String> {
        return mapOf(
            "accept" to "application/json",
            "Authorization" to AUTHORIZATION_CODE,
            "api_key" to API_KEY
        )
    }
}
