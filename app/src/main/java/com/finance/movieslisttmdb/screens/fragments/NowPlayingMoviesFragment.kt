package com.finance.movieslisttmdb.screens.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.FilterViewBinding
import com.finance.movieslisttmdb.databinding.FragmentNowPlayingMoviesBinding
import com.finance.movieslisttmdb.databinding.SortViewBinding
import com.finance.movieslisttmdb.model.Genre
import com.finance.movieslisttmdb.paging.LoaderAdapter
import com.finance.movieslisttmdb.screens.activities.MovieDetailActivity
import com.finance.movieslisttmdb.screens.adapters.MoviePagingAdapter
import com.finance.movieslisttmdb.utils.GridSpacingItemDecoration
import com.finance.movieslisttmdb.utils.OnItemClickListener
import com.finance.movieslisttmdb.utils.isInternetAvailable
import com.finance.movieslisttmdb.utils.showNoInternetDialog
import com.finance.movieslisttmdb.viewmodels.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NowPlayingMoviesFragment : Fragment(), OnItemClickListener<Int> {
    private var _binding: FragmentNowPlayingMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MoviePagingAdapter
    private val viewModel: MovieViewModel by viewModels()
    private val selectedGenres: MutableSet<Genre> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNowPlayingMoviesBinding.inflate(inflater, container, false)
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
        initSortBottomSheetDialog()
        initFilterBottomSheetDialog()
        setClickListener()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun setClickListener() {
        binding.sortButton.setOnClickListener {
            showSortView()
        }

        binding.filterButton.setOnClickListener {
            showFilterView()
        }
    }

    private fun showFilterView() {
        filterBottomSheetDialog.show()
    }

    private fun initFilterBottomSheetDialog() {
        filterViewBinding = FilterViewBinding.inflate(layoutInflater)

        filterBottomSheetDialog = BottomSheetDialog(requireActivity())
        filterBottomSheetDialog.setContentView(filterViewBinding.root)
        filterBottomSheetDialog.dismissWithAnimation = true
        viewModel.fetchGenres()
        viewModel.genresLiveData.observe(viewLifecycleOwner) { genres ->
            populateChipGroup(genres)
        }
        initFilterPageViews()
    }

    private lateinit var sortBottomSheetDialog: BottomSheetDialog
    private lateinit var filterBottomSheetDialog: BottomSheetDialog

    private var lastCheckedRadioButtonId = -1

    private lateinit var sortViewBinding: SortViewBinding
    private lateinit var filterViewBinding: FilterViewBinding

    private fun showSortView() {
        sortBottomSheetDialog.show()
    }

    private fun hideSortView() {
        sortBottomSheetDialog.dismiss()
    }

    private fun initFilterPageViews() {
        filterViewBinding.btApply.setOnClickListener {
            var withGenres: String =
                selectedGenres.joinToString(separator = "|") { it.id.toString() }

            val map = mapOf(
                "with_genres" to withGenres,
                "include_adult" to "false",
                "language" to "en-US",
                "page" to "1"
            )
            hideFilterView()
            setupSearchedRecyclerView(map)
        }
    }

    private fun hideFilterView() {
        filterBottomSheetDialog.dismiss()
    }

    private fun initSortBottomSheetDialog() {
        sortViewBinding = SortViewBinding.inflate(layoutInflater)
        viewModel.fetchGenres()
        viewModel.genresLiveData.observe(viewLifecycleOwner) { genres ->
            populateChipGroup(genres)
        }

        sortViewBinding.sortDescending.isChecked = true
        sortViewBinding.yes.isChecked = true
        viewModel.fetchGenres()

        sortBottomSheetDialog = BottomSheetDialog(requireActivity())
        sortBottomSheetDialog.setContentView(sortViewBinding.root)
        sortBottomSheetDialog.dismissWithAnimation = true
        lastCheckedRadioButtonId = -1
        initSortPageViews()
    }

    private fun populateChipGroup(genres: List<Genre>) {
        filterViewBinding.chipGroup.removeAllViews() // Clear existing chips
        for (genre in genres) {
            val chip = Chip(context)
            chip.text = genre.name
            chip.isCheckable = true
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedGenres.add(genre)
                } else {
                    selectedGenres.remove(genre)
                }
            }
            filterViewBinding.chipGroup.addView(chip)
        }
    }

    private fun initSortPageViews() {
        sortViewBinding.btApply.setOnClickListener {
            hideSortView()

            val checkedIdSortOrder = sortViewBinding.sortRadioGroup.checkedRadioButtonId

            val sortOrder = if (sortViewBinding.sortDescending.isChecked) "desc" else "asc"
            val sortBy = when (checkedIdSortOrder) {
                R.id.sortPopularity -> "popularity"
                R.id.sortRating -> "vote_average"
                R.id.sortReleaseDate -> "release_date"
                R.id.sortTitle -> "original_title"
                else -> "popularity"
            }
            val includeAdults = sortViewBinding.yes.isChecked

            // Create map
            val map = mapOf(
                "sort_by" to "$sortBy.$sortOrder",
                "language" to "en-US",
                "include_adult" to includeAdults.toString(),
                "page" to "1"
            )

            setupSearchedRecyclerView(map)
        }
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
        viewModel.nowPlayingMoviesList.observe(viewLifecycleOwner) {
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

    private fun setupSearchedRecyclerView(options: Map<String, String>) {
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
        viewModel.getSortedMovies(options).observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

}