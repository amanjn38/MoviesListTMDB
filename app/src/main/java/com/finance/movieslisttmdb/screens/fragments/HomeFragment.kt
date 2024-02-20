package com.finance.movieslisttmdb.screens.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.FragmentHomeBinding
import com.finance.movieslisttmdb.paging.LoaderAdapter
import com.finance.movieslisttmdb.screens.activities.MovieDetailActivity
import com.finance.movieslisttmdb.screens.activities.SearchMovieActivity
import com.finance.movieslisttmdb.screens.adapters.MoviePagingAdapter
import com.finance.movieslisttmdb.utils.GridSpacingItemDecoration
import com.finance.movieslisttmdb.utils.OnItemClickListener
import com.finance.movieslisttmdb.utils.isInternetAvailable
import com.finance.movieslisttmdb.utils.showNoInternetDialog
import com.finance.movieslisttmdb.viewmodels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener<Int> {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MoviePagingAdapter
    private val viewModel: MovieViewModel by viewModels()
    private var currentQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context?.let { isInternetAvailable(it) } == true) {
            setupRecyclerView()
        } else {
            hideProgressBar()
            context?.let { showNoInternetDialog(it) }
        }
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false // Expand the search view
            binding.searchView.requestFocus() // Request focus on the search view
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                performSearchAction(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText
                if (newText.isNullOrBlank()) {
                    binding.btApply.visibility = View.GONE
                } else {
                    binding.btApply.visibility = View.VISIBLE
                }
                return false
            }
        })

        binding.btApply.setOnClickListener {
            val intent = Intent(context, SearchMovieActivity::class.java)
            intent.putExtra("query", currentQuery)
            startActivity(intent)
        }
    }

    private fun performSearchAction(query: String?) {
        val intent = Intent(context, SearchMovieActivity::class.java)
        intent.putExtra("query", query)
        startActivity(intent)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setupRecyclerView() {

        adapter = MoviePagingAdapter(this)
        binding.recyclerView.adapter = adapter

        val spacing =
            resources.getDimensionPixelSize(R.dimen.grid_spacing) // set your desired spacing
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )

        viewModel.trendingMoviesList.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }

        CoroutineScope(Dispatchers.IO).launch {
            adapter.loadStateFlow.collect { loadState ->
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        // The initial load is in progress
                        showProgressBar()
                    }

                    is LoadState.Error -> {
                        // Handle error state
                        val errorMessage = (loadState.refresh as LoadState.Error).error.message
                        Toast.makeText(context, "Error $errorMessage", Toast.LENGTH_SHORT).show()
                    }

                    is LoadState.NotLoading -> {
                        // The initial load is complete
                        hideProgressBar()
                    }
                }
            }

        }
    }

    override fun onItemClick(movieId: Int) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra("movieId", movieId)
        startActivity(intent)
    }
}