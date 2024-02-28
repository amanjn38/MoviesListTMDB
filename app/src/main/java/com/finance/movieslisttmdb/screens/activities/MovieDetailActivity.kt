package com.finance.movieslisttmdb.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.networksdk.Resource
import com.finance.movieslisttmdb.databinding.ActivityMovieDetailBinding
import com.finance.movieslisttmdb.model.MovieCredits
import com.finance.movieslisttmdb.model.MovieDetail
import com.finance.movieslisttmdb.model.MovieImages
import com.finance.movieslisttmdb.screens.adapters.CastAdapter
import com.finance.movieslisttmdb.screens.adapters.CrewAdapter
import com.finance.movieslisttmdb.screens.adapters.GenreAdapter
import com.finance.movieslisttmdb.screens.adapters.ImageAdapter
import com.finance.movieslisttmdb.screens.adapters.ProductionCompaniesAdapter
import com.finance.movieslisttmdb.utils.Constants.Companion.BASE_URL_IMAGE
import com.finance.movieslisttmdb.utils.CustomGridLayoutManager
import com.finance.movieslisttmdb.utils.HttpUtils
import com.finance.movieslisttmdb.utils.OnItemClickListener
import com.finance.movieslisttmdb.utils.convertRating
import com.finance.movieslisttmdb.utils.formatDate
import com.finance.movieslisttmdb.utils.formatTime
import com.finance.movieslisttmdb.viewmodels.MovieViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity(), OnItemClickListener<Int> {

    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieId = intent.getIntExtra("movieId", -1)

        viewModel.movieDetails.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    // Handle success state`
                    val response = resource.data
                    val responseData = HttpUtils.asString(response?.data)
                    val gson = Gson()
                    val myDataObject = gson.fromJson(responseData, MovieDetail::class.java)
                    setupMovieDetails(myDataObject)
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Unknown error occurred"
                    Toast.makeText(this, "Error $errorMessage", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }

        viewModel.movieImages.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    // Handle success state`
                    val response = resource.data
                    val responseData = HttpUtils.asString(response?.data)
                    val gson = Gson()
                    val myDataObject = gson.fromJson(responseData, MovieImages::class.java)
                    setupMovieImages(myDataObject)
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Unknown error occurred"
                    Toast.makeText(this, "Error $errorMessage", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }


        viewModel.movieCredits.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    // Handle success state`
                    val response = resource.data
                    val responseData = HttpUtils.asString(response?.data)
                    val gson = Gson()
                    val myDataObject = gson.fromJson(responseData, MovieCredits::class.java)
                    setupMovieCredits(myDataObject)
                }

                is Resource.Error -> {
                    val errorMessage = resource.message ?: "Unknown error occurred"
                    Toast.makeText(this, "Error $errorMessage", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {

                }

                else -> {}
            }
        }

        viewModel.getMovieDetail(movieId.toString())
        viewModel.getMovieCredits(movieId.toString())
        viewModel.getMovieImages(movieId.toString())
    }


    private fun setupMovieDetails(movieDetail: MovieDetail) {

        Glide.with(this)
            .load(BASE_URL_IMAGE + movieDetail.backdropPath)
            .transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
            .centerCrop()
            .into(binding.imageViewBackDrop)

        Glide.with(this)
            .load(BASE_URL_IMAGE + movieDetail.posterPath)
            .transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
            .centerCrop()
            .into(binding.moviePoster)

        binding.movieTitle.text = movieDetail.title
        binding.movieLanguage.text = movieDetail.originalLanguage

        val layoutManager = CustomGridLayoutManager(this, 3) // Change the spanCount as needed
        binding.genreRecyclerView.layoutManager = layoutManager
        binding.genreRecyclerView.adapter = GenreAdapter(movieDetail.genres)

        binding.release.text = formatDate(movieDetail.releaseDate)
        binding.voteAvg.text = movieDetail.voteAverage.toString()
        binding.votes.text = movieDetail.voteCount.toString()
        binding.duration.text = formatTime(movieDetail.runtime)

        binding.overview.text = movieDetail.overview

        binding.productionCompaniesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.productionCompaniesRecyclerView.adapter =
            ProductionCompaniesAdapter(movieDetail.productionCompanies)

        binding.ratingBar.rating = convertRating(movieDetail.voteAverage.toString())
    }

    private fun setupMovieCredits(movieCredits: MovieCredits) {
        binding.crewSeeAllTxt.setOnClickListener {
            val intent = Intent(this, DisplayAllActivity::class.java)
            intent.putExtra("movieCredits", movieCredits)
            intent.putExtra("type", "crew")
            startActivity(intent)
        }

        binding.castSeeAllTxt.setOnClickListener {
            val intent = Intent(this, DisplayAllActivity::class.java)
            intent.putExtra("movieCredits", movieCredits)
            intent.putExtra("type", "cast")
            startActivity(intent)
        }
        binding.castRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.castRecyclerView.adapter = CastAdapter(movieCredits.cast, this)
        binding.castSeeAllTxt.text = "See All ${movieCredits.cast.size} -->"
        binding.crewRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.crewRecyclerView.adapter = CrewAdapter(movieCredits.crew, this)
        binding.crewSeeAllTxt.text = "See All ${movieCredits.crew.size} -->"

    }

    private fun setupMovieImages(movieImages: MovieImages) {
        binding.imagesSeeAllTxt.setOnClickListener {
            val intent = Intent(this, DisplayAllActivity::class.java)
            intent.putExtra("movieImages", movieImages)
            intent.putExtra("type", "images")
            startActivity(intent)
        }
        binding.imagesRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.imagesSeeAllTxt.text = "See All ${movieImages.backdrops.size} -->"

        if (movieImages.backdrops.isNullOrEmpty()) {

        } else {
            binding.imagesRecyclerView.adapter = ImageAdapter(movieImages.backdrops)
        }

    }

    override fun onItemClick(movieId: Int) {
        val intent = Intent(this, PeopleDetailActivity::class.java)
        intent.putExtra("peopleId", movieId)
        startActivity(intent)
    }
}