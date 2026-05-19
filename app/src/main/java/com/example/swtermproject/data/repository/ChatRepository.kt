package com.example.swtermproject.data.repository

import android.content.Context
import com.example.swtermproject.BuildConfig
import com.example.swtermproject.data.api.RetrofitClient
import com.example.swtermproject.data.db.AppDatabase
import com.example.swtermproject.data.db.entity.ChatHistoryEntity
import com.example.swtermproject.data.model.ChatMessage
import com.example.swtermproject.data.model.OpenAiRequest
import com.example.swtermproject.util.Constants

class ChatRepository(context: Context) {
    private val api = RetrofitClient.openAiApi
    private val dao = AppDatabase.getInstance(context).chatHistoryDao()

    private val systemMessage = ChatMessage(role = "system", content = Constants.SYSTEM_PROMPT)

    suspend fun sendMessage(history: List<ChatMessage>, userMessage: String): String {
        val messages = mutableListOf(systemMessage)
        messages.addAll(history.takeLast(10))
        messages.add(ChatMessage(role = "user", content = userMessage))

        val request = OpenAiRequest(model = Constants.OPENAI_MODEL, messages = messages)
        val response = api.sendMessage("Bearer ${BuildConfig.OPENAI_API_KEY}", request)
        return response.choices.first().message.content
    }

    suspend fun saveMessage(role: String, content: String) {
        dao.insert(ChatHistoryEntity(role = role, content = content))
    }

    suspend fun loadHistory(): List<ChatMessage> =
        dao.getAll().map { ChatMessage(role = it.role, content = it.content) }

    suspend fun clearHistory() = dao.clearAll()
}
