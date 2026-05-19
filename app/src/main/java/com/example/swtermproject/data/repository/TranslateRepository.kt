package com.example.swtermproject.data.repository

import com.example.swtermproject.BuildConfig
import com.example.swtermproject.data.api.RetrofitClient
import com.example.swtermproject.data.model.TranslationResult

class TranslateRepository {
    private val api = RetrofitClient.translateApi

    suspend fun translate(text: String, targetLanguage: String): TranslationResult {
        val response = api.translate(
            text = text,
            targetLanguage = targetLanguage,
            apiKey = BuildConfig.TRANSLATE_API_KEY
        )
        val item = response.data.translations.first()
        return TranslationResult(
            originalText = text,
            translatedText = item.translatedText,
            sourceLanguage = item.detectedSourceLanguage ?: "ko",
            targetLanguage = targetLanguage
        )
    }
}
