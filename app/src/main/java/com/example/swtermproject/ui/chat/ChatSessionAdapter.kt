package com.example.swtermproject.ui.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swtermproject.data.db.entity.ChatSessionEntity
import com.example.swtermproject.databinding.ItemChatSessionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatSessionAdapter(
    private val onClick: (ChatSessionEntity) -> Unit,
    private val onMoreClick: (ChatSessionEntity) -> Unit
) : ListAdapter<ChatSessionEntity, ChatSessionAdapter.SessionVH>(DIFF_CALLBACK) {

    private var selectedId: Long? = null
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatSessionEntity>() {
            override fun areItemsTheSame(old: ChatSessionEntity, new: ChatSessionEntity) = old.id == new.id
            override fun areContentsTheSame(old: ChatSessionEntity, new: ChatSessionEntity) = old == new
        }
    }

    fun setSelected(sessionId: Long?) {
        selectedId = sessionId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionVH {
        val binding = ItemChatSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SessionVH(binding)
    }

    override fun onBindViewHolder(holder: SessionVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SessionVH(private val b: ItemChatSessionBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: ChatSessionEntity) {
            b.tvSessionTitle.text = item.title
            b.tvSessionUpdated.text = formatter.format(Date(item.updatedAt))

            val highlight = item.id == selectedId
            val color = if (highlight) {
                ContextCompat.getColor(b.root.context, com.example.swtermproject.R.color.colorPrimaryLight)
            } else {
                Color.TRANSPARENT
            }
            b.sessionItemRoot.setBackgroundColor(color)

            b.sessionItemRoot.setOnClickListener { onClick(item) }
            b.btnSessionMore.setOnClickListener { onMoreClick(item) }
        }
    }
}
