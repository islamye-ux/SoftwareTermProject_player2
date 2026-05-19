package com.example.swtermproject.data.repository

import android.content.Context
import com.example.swtermproject.BuildConfig
import com.example.swtermproject.data.api.RetrofitClient
import com.example.swtermproject.data.db.AppDatabase
import com.example.swtermproject.data.db.entity.ChatHistoryEntity
import com.example.swtermproject.data.model.ChatMessage
import com.example.swtermproject.data.model.GeminiContent
import com.example.swtermproject.data.model.GeminiPart
import com.example.swtermproject.data.model.GeminiRequest
import com.example.swtermproject.data.model.GeminiSystemInstruction
import com.example.swtermproject.util.Constants

class ChatRepository(context: Context) {
    private val api = RetrofitClient.geminiApi
    private val dao = AppDatabase.getInstance(context).chatHistoryDao()

    suspend fun sendMessage(history: List<ChatMessage>, userMessage: String): String {
        val contents = mutableListOf<GeminiContent>()
        history.takeLast(10).forEach { msg ->
            val geminiRole = if (msg.role == "assistant") "model" else "user"
            contents.add(GeminiContent(role = geminiRole, parts = listOf(GeminiPart(msg.content))))
        }
        contents.add(GeminiContent(role = "user", parts = listOf(GeminiPart(userMessage))))

        val request = GeminiRequest(
            contents = contents,
            systemInstruction = GeminiSystemInstruction(
                parts = listOf(GeminiPart(Constants.SYSTEM_PROMPT))
            )
        )
        val response = api.generateContent(BuildConfig.GEMINI_API_KEY, request)
        return response.candidates.first().content.parts.first().text
    }

    suspend fun saveMessage(role: String, content: String) {
        dao.insert(ChatHistoryEntity(role = role, content = content))
    }

    suspend fun loadHistory(): List<ChatMessage> =
        dao.getAll().map { ChatMessage(role = it.role, content = it.content) }

    suspend fun clearHistory() = dao.clearAll()
}
