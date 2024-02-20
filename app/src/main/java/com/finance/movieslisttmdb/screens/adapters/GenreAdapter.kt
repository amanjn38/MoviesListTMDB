package com.finance.movieslisttmdb.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finance.movieslisttmdb.databinding.GenreItemLayoutBinding
import com.finance.movieslisttmdb.model.Genre

class GenreAdapter(
    private var items: List<Genre>
) : RecyclerView.Adapter<GenreAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GenreItemLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreAdapter.ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun updateGenres(newGenres: List<Genre>) {
        items = newGenres
        notifyDataSetChanged()
    }
    inner class ItemViewHolder(private val binding: GenreItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.apply {
                genreName.text = genre.name
            }
        }
    }
}