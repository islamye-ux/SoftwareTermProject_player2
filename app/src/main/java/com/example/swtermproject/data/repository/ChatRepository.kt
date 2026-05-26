package com.example.swtermproject.data.repository

import android.content.Context
import android.util.Log
import com.example.swtermproject.BuildConfig
import com.example.swtermproject.data.api.RetrofitClient
import retrofit2.HttpException
import com.example.swtermproject.data.db.AppDatabase
import com.example.swtermproject.data.db.entity.ChatHistoryEntity
import com.example.swtermproject.data.db.entity.ChatSessionEntity
import com.example.swtermproject.data.model.ChatMessage
import com.example.swtermproject.data.model.GeminiContent
import com.example.swtermproject.data.model.GeminiPart
import com.example.swtermproject.data.model.GeminiRequest
import com.example.swtermproject.data.model.GeminiSystemInstruction
import com.example.swtermproject.util.Constants

class ChatRepository(context: Context) {
    private val api = RetrofitClient.geminiApi
    private val dao = AppDatabase.getInstance(context).chatHistoryDao()
    private val sessionDao = AppDatabase.getInstance(context).chatSessionDao()

    private fun sanitizeText(text: String): String {
        return text
            .replace("```", "")
            .replace("`", "")
            .replace("*", "")
            .replace("#", "")
            .replace(Regex("\\p{Cs}"), "")
            .trim()
    }

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
        try {
            val response = api.generateContent(BuildConfig.GEMINI_API_KEY, request)
            val raw = response.candidates.first().content.parts.first().text
            return sanitizeText(raw)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("ChatRepository", "HTTP ${e.code()} - $errorBody")
            throw Exception("HTTP ${e.code()}: $errorBody")
        }
    }

    suspend fun saveMessage(role: String, content: String, sessionId: Long) {
        dao.insert(ChatHistoryEntity(sessionId = sessionId, role = role, content = content))
        sessionDao.touch(sessionId, System.currentTimeMillis())
    }

    suspend fun loadHistory(sessionId: Long): List<ChatMessage> =
        dao.getAllForSession(sessionId).map { ChatMessage(role = it.role, content = it.content) }

    suspend fun clearHistory(sessionId: Long) = dao.clearForSession(sessionId)

    suspend fun getSessions(query: String?): List<ChatSessionEntity> {
        return if (query.isNullOrBlank()) {
            sessionDao.getAll()
        } else {
            sessionDao.searchByTitleOrMessage("%${query.trim()}%")
        }
    }

    suspend fun createSession(title: String): Long {
        val now = System.currentTimeMillis()
        return sessionDao.insert(ChatSessionEntity(title = title, createdAt = now, updatedAt = now))
    }

    suspend fun renameSession(sessionId: Long, title: String) {
        sessionDao.rename(sessionId, title, System.currentTimeMillis())
    }

    suspend fun deleteSession(sessionId: Long) {
        dao.clearForSession(sessionId)
        sessionDao.delete(sessionId)
    }

    suspend fun getSession(sessionId: Long): ChatSessionEntity? = sessionDao.getById(sessionId)
}
