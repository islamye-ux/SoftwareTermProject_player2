package com.example.swtermproject.data.db.dao

import androidx.room.*
import com.example.swtermproject.data.db.entity.ChatHistoryEntity

@Dao
interface ChatHistoryDao {
    @Query("SELECT * FROM chat_history WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getAllForSession(sessionId: Long): List<ChatHistoryEntity>

    @Insert
    suspend fun insert(message: ChatHistoryEntity)

    @Query("DELETE FROM chat_history WHERE sessionId = :sessionId")
    suspend fun clearForSession(sessionId: Long)

    @Query("DELETE FROM chat_history")
    suspend fun clearAll()
}
