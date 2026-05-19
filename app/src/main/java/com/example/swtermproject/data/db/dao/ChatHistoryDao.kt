package com.example.swtermproject.data.db.dao

import androidx.room.*
import com.example.swtermproject.data.db.entity.ChatHistoryEntity

@Dao
interface ChatHistoryDao {
    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC")
    suspend fun getAll(): List<ChatHistoryEntity>

    @Insert
    suspend fun insert(message: ChatHistoryEntity)

    @Query("DELETE FROM chat_history")
    suspend fun clearAll()
}
