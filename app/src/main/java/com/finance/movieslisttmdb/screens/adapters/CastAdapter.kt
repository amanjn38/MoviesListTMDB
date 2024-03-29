package com.finance.movieslisttmdb.screens.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.finance.movieslisttmdb.databinding.CrewLayoutBinding
import com.finance.movieslisttmdb.model.Cast
import com.finance.movieslisttmdb.utils.Constants
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.utils.OnItemClickListener

class CastAdapter(
    private val items: List<Cast>,
    private val itemClicked: OnItemClickListener<Int>
) : RecyclerView.Adapter<CastAdapter.ItemViewHolder>() {
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

        fun bind(cast: Cast) {
            binding.apply {
                if (cast.profilePath.isNullOrEmpty()) {

                } else {
                    val url = Constants.BASE_URL_IMAGE + cast.profilePath

                    Glide.with(itemView)
                        .load(url)
                        .placeholder(R.drawable.user)
                        .apply(RequestOptions().transform(CircleCrop()))
                        .into(binding.personPhoto)

                }

                binding.personName.text = cast.name
                binding.personRole.text = cast.character

                parent.setOnClickListener {
                    itemClicked.onItemClick(cast.id)
                }
            }
        }
    }
}