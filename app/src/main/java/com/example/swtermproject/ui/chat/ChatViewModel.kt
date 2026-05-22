package com.example.swtermproject.ui.chat

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.data.db.entity.ChatSessionEntity
import com.example.swtermproject.data.model.ChatMessage
import com.example.swtermproject.data.repository.ChatRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    private val _sessions = MutableLiveData<List<ChatSessionEntity>>(emptyList())
    val sessions: LiveData<List<ChatSessionEntity>> = _sessions

    private val _currentSessionId = MutableLiveData<Long>()
    val currentSessionId: LiveData<Long> = _currentSessionId

    private val _currentSessionTitle = MutableLiveData<String>()
    val currentSessionTitle: LiveData<String> = _currentSessionTitle

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var repository: ChatRepository
    private var currentQuery: String? = null

    fun init(context: Context) {
        repository = ChatRepository(context)
        loadInitialSessions()
    }

    private fun loadInitialSessions() {
        viewModelScope.launch {
            val list = repository.getSessions(null)
            if (list.isEmpty()) {
                val newId = repository.createSession(defaultTitle())
                _sessions.value = repository.getSessions(null)
                selectSession(newId)
            } else {
                _sessions.value = list
                selectSession(list.first().id)
            }
        }
    }

    fun sendMessage(userText: String) {
        viewModelScope.launch {
            val title = defaultTitle()
            val sessionId = _currentSessionId.value ?: repository.createSession(title).also {
                _sessions.value = repository.getSessions(currentQuery)
                _currentSessionId.value = it
                _currentSessionTitle.value = title
            }
            val list = _messages.value ?: mutableListOf()
            list.add(ChatMessage(role = "user", content = userText))
            _messages.value = list
            repository.saveMessage("user", userText, sessionId)

            _isLoading.value = true
            try {
                val reply = repository.sendMessage(list.dropLast(1), userText)
                list.add(ChatMessage(role = "assistant", content = reply))
                _messages.value = list
                repository.saveMessage("assistant", reply, sessionId)
                refreshSessions()
            } catch (e: Exception) {
                list.add(ChatMessage(role = "assistant", content = "⚠️ ${e.message}"))
                _messages.value = list
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearChatMessages() {
        viewModelScope.launch {
            val sessionId = _currentSessionId.value ?: return@launch
            repository.clearHistory(sessionId)
            _messages.value = mutableListOf()
            refreshSessions()
        }
    }

    fun newChat() {
        viewModelScope.launch {
            val newId = repository.createSession(defaultTitle())
            refreshSessions()
            selectSession(newId)
        }
    }

    fun searchSessions(query: String?) {
        currentQuery = query
        viewModelScope.launch {
            _sessions.value = repository.getSessions(query)
        }
    }

    fun selectSession(sessionId: Long) {
        viewModelScope.launch {
            val session = repository.getSession(sessionId) ?: return@launch
            _currentSessionId.value = sessionId
            _currentSessionTitle.value = session.title
            val history = repository.loadHistory(sessionId)
            _messages.value = history.toMutableList()
        }
    }

    fun renameSession(sessionId: Long, title: String) {
        viewModelScope.launch {
            repository.renameSession(sessionId, title)
            if (_currentSessionId.value == sessionId) {
                _currentSessionTitle.value = title
            }
            refreshSessions()
        }
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            val list = repository.getSessions(currentQuery)
            _sessions.value = list
            if (list.isEmpty()) {
                val newId = repository.createSession(defaultTitle())
                refreshSessions()
                selectSession(newId)
            } else if (_currentSessionId.value == sessionId) {
                selectSession(list.first().id)
            }
        }
    }

    private suspend fun refreshSessions() {
        _sessions.value = repository.getSessions(currentQuery)
    }

    private fun defaultTitle(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return "Chat ${formatter.format(Date())}"
    }
}
