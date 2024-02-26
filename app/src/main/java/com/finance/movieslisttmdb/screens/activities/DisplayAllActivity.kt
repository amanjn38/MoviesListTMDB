package com.finance.movieslisttmdb.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.ActivityDisplayAllBinding
import com.finance.movieslisttmdb.databinding.ActivityMovieDetailBinding
import com.finance.movieslisttmdb.model.MovieCredits
import com.finance.movieslisttmdb.model.MovieImages
import com.finance.movieslisttmdb.screens.adapters.CastAdapter
import com.finance.movieslisttmdb.screens.adapters.CrewAdapter
import com.finance.movieslisttmdb.screens.adapters.GenreAdapter
import com.finance.movieslisttmdb.screens.adapters.ImageAdapter
import com.finance.movieslisttmdb.utils.CustomGridLayoutManager

class DisplayAllActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayAllBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_all)
        binding = ActivityDisplayAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras = intent.extras
        if (extras != null) {
            // Retrieve movieCredits object

            // Retrieve the type
            val type = extras.getString("type")
            if (type == "crew") {
                val movieCredits = extras.getSerializable("movieCredits") as? MovieCredits

                val layoutManager =
                    GridLayoutManager(this, 3) // Change the spanCount as needed
                binding.recyclerView.layoutManager = layoutManager
                if (movieCredits != null) {
                    binding.recyclerView.adapter = CrewAdapter(movieCredits.crew)
                }
            } else if (type == "cast") {
                val movieCredits = extras.getSerializable("movieCredits") as? MovieCredits

                val layoutManager =
                    GridLayoutManager(this, 3) // Change the spanCount as needed
                binding.recyclerView.layoutManager = layoutManager
                if (movieCredits != null) {
                    binding.recyclerView.adapter = CastAdapter(movieCredits.cast)
                }
            } else if (type == "images") {
                val movieImages = extras.getSerializable("movieImages") as? MovieImages
                binding.recyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                if (movieImages != null) {
                    binding.recyclerView.adapter = ImageAdapter(movieImages.backdrops)
                }

            }
        }
    }
}