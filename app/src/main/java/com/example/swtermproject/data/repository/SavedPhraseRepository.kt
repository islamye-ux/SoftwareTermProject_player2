package com.example.swtermproject.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.swtermproject.data.db.AppDatabase
import com.example.swtermproject.data.db.entity.SavedPhraseEntity

class SavedPhraseRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).savedPhraseDao()

    fun getAll(): LiveData<List<SavedPhraseEntity>> = dao.getAll()

    suspend fun addPhrase(original: String, translated: String, targetLanguage: String) {
        dao.insert(
            SavedPhraseEntity(
                originalText = original,
                translatedText = translated,
                targetLanguage = targetLanguage
            )
        )
    }

    suspend fun removeById(id: Long) {
        dao.deleteById(id)
    }
}
