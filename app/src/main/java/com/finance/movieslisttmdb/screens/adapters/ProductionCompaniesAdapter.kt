package com.finance.movieslisttmdb.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.finance.movieslisttmdb.databinding.ImageLayoutBinding
import com.finance.movieslisttmdb.model.Backdrop
import com.finance.movieslisttmdb.model.ProductionCompany
import com.finance.movieslisttmdb.utils.Constants

class ProductionCompaniesAdapter(
    private val items: List<ProductionCompany>
) : RecyclerView.Adapter<ProductionCompaniesAdapter.ItemViewHolder>() {
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

        fun bind(productionCompany: ProductionCompany) {
            binding.apply {
                binding.apply {
                    if (productionCompany.logoPath.isNullOrEmpty()) {

                    }else{
                        Glide.with(itemView)
                            .load(Constants.BASE_URL_IMAGE + productionCompany.logoPath)
                            .transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
                            .centerCrop()
                            .into(binding.image)

                    }

                    binding.productionCompanyName.text = productionCompany.name
                }
            }
        }
    }
}