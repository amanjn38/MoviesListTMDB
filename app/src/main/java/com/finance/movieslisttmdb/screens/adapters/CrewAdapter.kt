package com.finance.movieslisttmdb.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.CrewLayoutBinding
import com.finance.movieslisttmdb.model.Crew
import com.finance.movieslisttmdb.utils.Constants
import com.finance.movieslisttmdb.utils.OnItemClickListener

class CrewAdapter(
    private val items: List<Crew>,
    private val itemClicked: OnItemClickListener<Int>
) : RecyclerView.Adapter<CrewAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CrewLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size


    inner class ItemViewHolder(private val binding: CrewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crew: Crew) {
            binding.apply {
                if (crew.profilePath.isNullOrEmpty()) {


                } else {
                    val url = Constants.BASE_URL_IMAGE + crew.profilePath
                    Glide.with(itemView)
                        .load(url)
                        .placeholder(R.drawable.user)
                        .apply(RequestOptions().transform(CircleCrop()))
                        .into(binding.personPhoto)
                }

                binding.personName.text = crew.name
                binding.personRole.text = crew.job

                parent.setOnClickListener {
                    itemClicked.onItemClick(crew.id)
                }
            }
        }
    }
}