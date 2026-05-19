package com.example.swtermproject.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swtermproject.data.model.PlaceModel
import com.example.swtermproject.databinding.ItemPlaceBinding

class PlaceAdapter(
    private val onClick: (PlaceModel) -> Unit
) : ListAdapter<PlaceModel, PlaceAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaceModel>() {
            override fun areItemsTheSame(old: PlaceModel, new: PlaceModel) = old.id == new.id
            override fun areContentsTheSame(old: PlaceModel, new: PlaceModel) = old == new
        }
    }

    inner class ViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: PlaceModel) {
            binding.tvName.text = place.name
            binding.tvAddress.text = place.address
            binding.tvRating.text = if (place.rating > 0) "★ ${place.rating}" else ""
            binding.root.setOnClickListener { onClick(place) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}
