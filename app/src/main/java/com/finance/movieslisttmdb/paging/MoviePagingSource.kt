package com.finance.movieslisttmdb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.finance.movieslisttmdb.model.Movie
import com.finance.movieslisttmdb.repository.MovieRepository
import com.finance.movieslisttmdb.utils.HttpUtils
import com.google.gson.Gson

class MoviePagingSource(
    private val movieRepository: MovieRepository,
    private val movieType: String,
    private val query: Map<String, String>
) : PagingSource<Int, com.finance.movieslisttmdb.model.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.finance.movieslisttmdb.model.Result> {
        return try {
            val position = params.key ?: 1
            val queryMap = mapOf(
                "page" to position.toString()
            )
            val response = when (movieType) {
                "nowPlaying" -> {
                    movieRepository.getNowPlayingMovies(position, queryMap)
                }

                "popular" -> {
                    movieRepository.getPopularMovies(position, queryMap)
                }

                "trending" -> {
                    movieRepository.getTrendingMovies(position, queryMap)
                }

                "sorted" -> {
                    movieRepository.discoverMovies(position, query)
                }

                else -> {
                    movieRepository.searchMovies(position, query)
                }
            }

            val responseData = HttpUtils.asString(response.data?.data)
            val gson = Gson()
            val myDataObject = gson.fromJson(responseData, Movie::class.java)
            return LoadResult.Page(
                data = myDataObject.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == myDataObject.totalPages) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, com.finance.movieslisttmdb.model.Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}