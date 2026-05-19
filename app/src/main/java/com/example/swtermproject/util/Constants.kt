package com.example.swtermproject.util

object Constants {
    const val EXTRA_LANGUAGE_CODE = "extra_language_code"
    const val EXTRA_TRANSLATION_RESULT = "extra_translation_result"
    const val EXTRA_PLACE_ID = "extra_place_id"
    const val EXTRA_PLACE_NAME = "extra_place_name"
    const val EXTRA_PLACE_ADDRESS = "extra_place_address"
    const val EXTRA_PLACE_LAT = "extra_place_lat"
    const val EXTRA_PLACE_LNG = "extra_place_lng"
    const val EXTRA_PLACE_RATING = "extra_place_rating"
    const val EXTRA_PLACE_CATEGORY = "extra_place_category"

    const val DEFAULT_LANGUAGE = "en"

    const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"
    const val TRANSLATE_BASE_URL = "https://translation.googleapis.com/"
    const val SYSTEM_PROMPT = """You are a helpful assistant for foreigners living in Korea.
Answer questions about daily life in Korea such as visa, healthcare, banking,
transportation, housing, and culture. Always answer in the user's language
based on the language of their question. Be concise and practical."""
}
