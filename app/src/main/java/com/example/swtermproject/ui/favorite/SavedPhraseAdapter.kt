package com.example.swtermproject.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swtermproject.data.db.entity.SavedPhraseEntity
import com.example.swtermproject.databinding.ItemSavedPhraseBinding

class SavedPhraseAdapter(
    private val onDelete: (SavedPhraseEntity) -> Unit
) : ListAdapter<SavedPhraseEntity, SavedPhraseAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SavedPhraseEntity>() {
            override fun areItemsTheSame(old: SavedPhraseEntity, new: SavedPhraseEntity) = old.id == new.id
            override fun areContentsTheSame(old: SavedPhraseEntity, new: SavedPhraseEntity) = old == new
        }
    }

    inner class ViewHolder(private val binding: ItemSavedPhraseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SavedPhraseEntity) {
            binding.tvOriginal.text = item.originalText
            binding.tvTranslated.text = item.translatedText
            binding.btnDeletePhrase.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemSavedPhraseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}
