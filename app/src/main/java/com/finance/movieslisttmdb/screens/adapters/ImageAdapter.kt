package com.finance.movieslisttmdb.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.finance.movieslisttmdb.databinding.ImageLayoutBinding
import com.finance.movieslisttmdb.model.Backdrop
import com.finance.movieslisttmdb.utils.Constants

class ImageAdapter(
    private val items: List<Backdrop>
) : RecyclerView.Adapter<ImageAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size


    inner class ItemViewHolder(private val binding: ImageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(backdrop: Backdrop) {
            binding.apply {
                binding.apply {
                    if (backdrop.filePath.isNotEmpty()) {
                        Glide.with(itemView)
                            .load(Constants.BASE_URL_IMAGE + backdrop.filePath)
                            .transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
                            .centerCrop()
                            .into(binding.image)
                    }
                }
            }
        }
    }
}