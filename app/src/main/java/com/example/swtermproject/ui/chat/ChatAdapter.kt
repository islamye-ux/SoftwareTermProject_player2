package com.example.swtermproject.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swtermproject.data.model.ChatMessage
import com.example.swtermproject.databinding.ItemChatAiBinding
import com.example.swtermproject.databinding.ItemChatUserBinding

class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val TYPE_USER = 0
        private const val TYPE_AI = 1
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(old: ChatMessage, new: ChatMessage) = old === new
            override fun areContentsTheSame(old: ChatMessage, new: ChatMessage) = old == new
        }
    }

    override fun getItemViewType(position: Int) =
        if (getItem(position).role == "user") TYPE_USER else TYPE_AI

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_USER) {
            UserVH(ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            AiVH(ItemChatAiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserVH -> holder.bind(getItem(position))
            is AiVH -> holder.bind(getItem(position))
        }
    }

    inner class UserVH(private val b: ItemChatUserBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(msg: ChatMessage) { b.tvMessage.text = msg.content }
    }

    inner class AiVH(private val b: ItemChatAiBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(msg: ChatMessage) { b.tvMessage.text = msg.content }
    }
}
