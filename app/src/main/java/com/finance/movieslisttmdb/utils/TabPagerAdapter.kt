package com.finance.movieslisttmdb.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finance.movieslisttmdb.screens.fragments.HomeFragment
import com.finance.movieslisttmdb.screens.fragments.NowPlayingMoviesFragment
import com.finance.movieslisttmdb.screens.fragments.PopularMoviesFragment

class TabPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> NowPlayingMoviesFragment()
            2 -> PopularMoviesFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
