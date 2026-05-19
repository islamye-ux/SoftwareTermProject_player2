package com.example.swtermproject.ui.chat

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.data.model.ChatMessage
import com.example.swtermproject.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var repository: ChatRepository

    fun init(context: Context) {
        repository = ChatRepository(context)
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val history = repository.loadHistory()
            _messages.value = history.toMutableList()
        }
    }

    fun sendMessage(userText: String) {
        viewModelScope.launch {
            val list = _messages.value ?: mutableListOf()
            list.add(ChatMessage(role = "user", content = userText))
            _messages.value = list
            repository.saveMessage("user", userText)

            _isLoading.value = true
            try {
                val reply = repository.sendMessage(list.dropLast(1), userText)
                list.add(ChatMessage(role = "assistant", content = reply))
                _messages.value = list
                repository.saveMessage("assistant", reply)
            } catch (e: Exception) {
                list.add(ChatMessage(role = "assistant", content = "⚠️ ${e.message}"))
                _messages.value = list
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearHistory()
            _messages.value = mutableListOf()
        }
    }
}
