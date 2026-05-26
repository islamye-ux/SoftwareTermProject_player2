package com.example.swtermproject.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swtermproject.R

class PlaceTypeAdapter(
    private val original: List<Pair<String, String>>,
    private val onClick: (String, String) -> Unit
) : RecyclerView.Adapter<PlaceTypeAdapter.VH>() {

    private var items: List<Pair<String, String>> = original

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.tvPlaceType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_place_type, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val (label, type) = items[position]
        holder.tv.text = label
        holder.itemView.setOnClickListener { onClick(label, type) }
    }

    override fun getItemCount(): Int = items.size

    fun filter(query: String) {
        val q = query.trim().lowercase()
        items = if (q.isEmpty()) original else original.filter { it.first.lowercase().contains(q) || it.second.lowercase().contains(q) }
        notifyDataSetChanged()
    }

    fun submitList(list: List<Pair<String, String>>) {
        // not used but available
        items = list
        notifyDataSetChanged()
    }
}
