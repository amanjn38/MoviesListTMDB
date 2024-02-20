package com.finance.movieslisttmdb.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.ActivitySearchMovieBinding
import com.finance.movieslisttmdb.paging.LoaderAdapter
import com.finance.movieslisttmdb.screens.adapters.MoviePagingAdapter
import com.finance.movieslisttmdb.utils.GridSpacingItemDecoration
import com.finance.movieslisttmdb.utils.OnItemClickListener
import com.finance.movieslisttmdb.viewmodels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieActivity : AppCompatActivity(), OnItemClickListener<Int> {
    private lateinit var binding: ActivitySearchMovieBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MoviePagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val query = intent.getStringExtra("query")
        val map = mapOf(
            "query" to query.toString(),
            "language" to "en-US",
            "include_adult" to "false",
            "page" to "1"
        )
        setupRecyclerView(map)

    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setupRecyclerView(query: Map<String, String>) {

        adapter = MoviePagingAdapter(this)
        binding.recyclerView.adapter = adapter

        val spacing =
            resources.getDimensionPixelSize(R.dimen.grid_spacing) // set your desired spacing
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )

        viewModel.getSearchedMoviesByQuery(query).observe(this) {
            hideProgressBar()
            adapter.submitData(lifecycle, it)
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            adapter.loadStateFlow.collect { loadState ->
//                when (loadState.refresh) {
//                    is LoadState.Loading -> {
//                        // The initial load is in progress
//                        showProgressBar()
//                    }
//
//                    is LoadState.Error -> {
//                        // Handle error state
//                        val errorMessage = (loadState.refresh as LoadState.Error).error.message
////                        showError(errorMessage)
//                    }
//
//                    is LoadState.NotLoading -> {
//                        // The initial load is complete
//                        hideProgressBar()
//                    }
//                }
//            }
//
//        }
    }

    override fun onItemClick(movieId: Int) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movieId", movieId)
        startActivity(intent)
    }

}