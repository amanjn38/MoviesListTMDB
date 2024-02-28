package com.finance.movieslisttmdb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.custom.networksdk.Resource
import com.custom.networksdk.Response
import com.finance.movieslisttmdb.di.Helper
import com.finance.movieslisttmdb.model.Genre
import com.finance.movieslisttmdb.model.Result
import com.finance.movieslisttmdb.repository.GenreRepository
import com.finance.movieslisttmdb.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val helper: Helper,
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _movieDetails = MutableLiveData<Resource<Response>>()
    val movieDetails: LiveData<Resource<Response>> get() = _movieDetails

    private val _movieImages = MutableLiveData<Resource<Response>>()
    val movieImages: LiveData<Resource<Response>> get() = _movieImages

    private val _movieCredits = MutableLiveData<Resource<Response>>()
    val movieCredits: LiveData<Resource<Response>> get() = _movieCredits

    private val _genresLiveData = MutableLiveData<List<Genre>>()
    val genresLiveData: LiveData<List<Genre>> = _genresLiveData

    private val _peopleDetail = MutableLiveData<Resource<Response>>()
    val peopleDetail: LiveData<Resource<Response>> get() = _peopleDetail
    fun fetchGenres() {
        viewModelScope.launch {
            val genres = genreRepository.getGenres()
            _genresLiveData.postValue(genres)
        }
    }

    fun getMovieDetail(movieID: String) {
        viewModelScope.launch {
            _movieDetails.value = movieRepository.getMovieDetail(movieID)
        }
    }

    fun getMovieImages(movieID: String) {
        viewModelScope.launch {
            _movieImages.value = movieRepository.getMovieImages(movieID)
        }
    }

    fun getPersonDetail(personId: String){
        viewModelScope.launch {
            _peopleDetail.value = movieRepository.getPersonDetail(personId)
        }
    }

    fun getMovieCredits(movieID: String) {
        viewModelScope.launch {
            _movieCredits.value = movieRepository.getMovieCredits(movieID)
        }
    }

    val nowPlayingMoviesList = helper.getNowPlayingMovies().cachedIn(viewModelScope)
    val popularMoviesList = helper.getPopularMovies().cachedIn(viewModelScope)
    val trendingMoviesList = helper.getTrendingMovies().cachedIn(viewModelScope)

    fun getSortedMovies(query: Map<String, String>): LiveData<PagingData<Result>>{
        return helper.getSortedMovies(query = query).cachedIn(viewModelScope)
    }

    fun getSearchedMoviesByQuery(query: Map<String, String>): LiveData<PagingData<Result>>{
        return helper.getSearchedMovies(query = query).cachedIn(viewModelScope)
    }
}
