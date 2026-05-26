package com.example.swtermproject.ui.phrase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.swtermproject.BuildConfig
import com.example.swtermproject.data.api.RetrofitClient
import com.example.swtermproject.data.db.entity.SavedPhraseEntity
import com.example.swtermproject.data.repository.SavedPhraseRepository
import kotlinx.coroutines.launch

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SavedPhraseRepository(application)
    private val api = RetrofitClient.translateApi
    private val apiKey = BuildConfig.TRANSLATE_API_KEY

    val translated = MutableLiveData<String>()
    val error = MutableLiveData<String?>()
    val savedPhrases: LiveData<List<SavedPhraseEntity>> = repo.getAll()

    fun translate(text: String, targetLanguage: String) {
        viewModelScope.launch {
            try {
                error.postValue(null)
                val resp = api.translate(text, targetLanguage, apiKey)
                val translatedText = resp.data.translations.firstOrNull()?.translatedText ?: ""
                translated.postValue(translatedText)
                // save to recents
                repo.addPhrase(text, translatedText, targetLanguage)
            } catch (e: Exception) {
                error.postValue("Translation failed: ${e.message}")
            }
        }
    }

    fun removeSavedPhrase(entity: SavedPhraseEntity) {
        viewModelScope.launch { repo.removeById(entity.id) }
    }
}
