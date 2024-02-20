package com.finance.movieslisttmdb.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.finance.movieslisttmdb.databinding.ActivityMainBinding
import com.finance.movieslisttmdb.utils.TabPagerAdapter
import com.finance.movieslisttmdb.viewmodels.MovieViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = TabPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Home"
                1 -> "Latest"
                2 -> "Popular"
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()

        viewModel.fetchGenres()

    }
}