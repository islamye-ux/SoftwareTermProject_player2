package com.example.swtermproject.data.model

data class TranslationResult(
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String
)

data class TranslateApiResponse(
    val data: TranslateData
)

data class TranslateData(
    val translations: List<TranslationItem>
)

data class TranslationItem(
    val translatedText: String,
    val detectedSourceLanguage: String? = null
)
