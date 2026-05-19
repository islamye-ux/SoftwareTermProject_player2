package com.example.swtermproject.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swtermproject.data.db.entity.FavoritePlaceEntity
import com.example.swtermproject.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onDelete: (FavoritePlaceEntity) -> Unit
) : ListAdapter<FavoritePlaceEntity, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoritePlaceEntity>() {
            override fun areItemsTheSame(old: FavoritePlaceEntity, new: FavoritePlaceEntity) =
                old.placeId == new.placeId
            override fun areContentsTheSame(old: FavoritePlaceEntity, new: FavoritePlaceEntity) =
                old == new
        }
    }

    inner class ViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: FavoritePlaceEntity) {
            binding.tvName.text = place.name
            binding.tvAddress.text = place.address
            binding.tvRating.text = if (place.rating > 0) "★ ${place.rating}" else ""
            binding.btnDelete.setOnClickListener { onDelete(place) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}
