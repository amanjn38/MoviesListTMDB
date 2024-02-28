package com.finance.movieslisttmdb.screens.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.custom.networksdk.Resource
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.ActivityPeopleDetailBinding
import com.finance.movieslisttmdb.model.Genre
import com.finance.movieslisttmdb.model.People
import com.finance.movieslisttmdb.screens.adapters.GenreAdapter
import com.finance.movieslisttmdb.utils.Constants
import com.finance.movieslisttmdb.utils.HttpUtils
import com.finance.movieslisttmdb.viewmodels.MovieViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeopleDetailBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val peopleId = intent.getIntExtra("peopleId", -1)

        viewModel.peopleDetail.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    // Handle success state`
                    val response = resource.data
                    val responseData = HttpUtils.asString(response?.data)
                    val gson = Gson()
                    val myDataObject = gson.fromJson(responseData, People::class.java)
                    setUpPeopleDetails(myDataObject)
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

        viewModel.getPersonDetail(peopleId.toString())
    }

    private fun setUpPeopleDetails(people: People) {
        with(binding) {

            if (people.profilePath.isNotEmpty()) {
                val url = Constants.BASE_URL_IMAGE + people.profilePath
                Glide.with(this@PeopleDetailActivity)
                    .load(url)
                    .placeholder(R.drawable.user)
                    .apply(RequestOptions().transform(CircleCrop()))
                    .into(binding.dp)
            }

            personName.text = people.name
            biography.text = people.biography
            birthPlace.text = "Birthplace: ${people.placeOfBirth}"
            birthDate.text = "BirthDate: ${people.birthday}"
            personKnownForDepartment.text = people.knownForDepartment

            val layoutManager =
                GridLayoutManager(this@PeopleDetailActivity, 3) // Change the spanCount as needed
            binding.alsoKnownAs.layoutManager = layoutManager
            val list = mutableListOf<Genre>()
            for (item in people.alsoKnownAs) {
                val genre = Genre(1, item)
                list.add(genre)
            }
            binding.alsoKnownAs.adapter = GenreAdapter(list)
            binding.openInNew.setOnClickListener {
                // IMDb website URL
                val imdbUrl = "https://www.imdb.com/name/${people.imdbId}"

                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(imdbUrl))
                startActivity(i)
            }
        }
    }
}