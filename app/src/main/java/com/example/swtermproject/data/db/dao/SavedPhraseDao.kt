package com.example.swtermproject.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.swtermproject.data.db.entity.SavedPhraseEntity

@Dao
interface SavedPhraseDao {
    @Query("SELECT * FROM saved_phrases ORDER BY savedAt DESC")
    fun getAll(): LiveData<List<SavedPhraseEntity>>

    @Insert
    suspend fun insert(entity: SavedPhraseEntity)

    @Query("DELETE FROM saved_phrases WHERE id = :id")
    suspend fun deleteById(id: Long)
}
