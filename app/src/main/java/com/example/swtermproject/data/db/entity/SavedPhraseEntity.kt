package com.example.swtermproject.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_phrases")
data class SavedPhraseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalText: String,
    val translatedText: String,
    val targetLanguage: String,
    val savedAt: Long = System.currentTimeMillis()
)
