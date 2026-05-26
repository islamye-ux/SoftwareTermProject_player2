package com.example.swtermproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.swtermproject.data.db.entity.ChatSessionEntity

@Dao
interface ChatSessionDao {
    @Query("SELECT * FROM chat_sessions ORDER BY updatedAt DESC")
    suspend fun getAll(): List<ChatSessionEntity>

    @Query(
        """
        SELECT DISTINCT s.* FROM chat_sessions s
        LEFT JOIN chat_history h ON s.id = h.sessionId
        WHERE s.title LIKE :query OR h.content LIKE :query
        ORDER BY s.updatedAt DESC
        """
    )
    suspend fun searchByTitleOrMessage(query: String): List<ChatSessionEntity>

    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getById(sessionId: Long): ChatSessionEntity?

    @Insert
    suspend fun insert(session: ChatSessionEntity): Long

    @Update
    suspend fun update(session: ChatSessionEntity)

    @Query("UPDATE chat_sessions SET title = :title, updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun rename(sessionId: Long, title: String, updatedAt: Long)

    @Query("UPDATE chat_sessions SET updatedAt = :updatedAt WHERE id = :sessionId")
    suspend fun touch(sessionId: Long, updatedAt: Long)

    @Query("DELETE FROM chat_sessions WHERE id = :sessionId")
    suspend fun delete(sessionId: Long)
}
