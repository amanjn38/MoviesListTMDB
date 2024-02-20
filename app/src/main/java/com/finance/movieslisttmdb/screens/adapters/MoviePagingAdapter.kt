package com.finance.movieslisttmdb.screens.adapters

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.MovieItemLayoutBinding
import com.finance.movieslisttmdb.model.Result
import com.finance.movieslisttmdb.utils.Constants
import com.finance.movieslisttmdb.utils.OnItemClickListener
import com.finance.movieslisttmdb.utils.formatDate

class MoviePagingAdapter(private val itemClicked: OnItemClickListener<Int>) :
    PagingDataAdapter<Result, MoviePagingAdapter.ItemViewHolder>(COMPARATOR) {

    inner class ItemViewHolder(private val binding: MovieItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result) {
            binding.apply {
                movieTitle.text = item.title

                val voteAvgPercentage = item.voteAverage * 10

                val progressColor = when {
                    voteAvgPercentage == -1.0 -> R.color.progress_gray // NA
                    voteAvgPercentage > 70.0 -> R.color.progress_green // Greater than 70
                    voteAvgPercentage in 40.0..70.0 -> R.color.progress_yellow // Between 40 and 70
                    else -> R.color.progress_red // Less than 40
                }

                val progressDrawable: Drawable? = progressBar.progressDrawable

                if (progressDrawable is LayerDrawable) {

                    // Assuming the index of the drawable you want to change is known
                    val index = 1 // This would be the index of your second item in the layer list

                    // Get the drawable at the specified index
                    val drawableToModify = progressDrawable.getDrawable(index)
                    drawableToModify.colorFilter = PorterDuffColorFilter(
                        ContextCompat.getColor(
                            itemView.context,
                            progressColor
                        ), PorterDuff.Mode.SRC_IN
                    )


                    progressBar.progressDrawable = progressDrawable
                }
                if (voteAvgPercentage == 0.0) {
                    progressBar.progress = 100
                    ratingPercentage.text = "NA"
                }
                progressBar.progress = voteAvgPercentage.toInt();

                ratingPercentage.text = voteAvgPercentage.toInt().toString() + "%"
                Glide.with(itemView)
                    .load(Constants.BASE_URL_IMAGE + item.posterPath)
                    .transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
                    .centerCrop()
                    .into(moviePoster)

                if (item.releaseDate != "") {
                    val releaseDate = formatDate(item.releaseDate)
                    movieReleaseDate.text = releaseDate
                }

                parent.setOnClickListener {
                    itemClicked.onItemClick(item.id)
                }
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.originalTitle == newItem.originalTitle
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }
}