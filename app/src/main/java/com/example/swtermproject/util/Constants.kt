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
    const val SYSTEM_PROMPT = """You are an assistant inside an Android mobile app that helps foreigners in Korea.
Context: This is a mobile Android app for travelers and residents; provide concise practical help about daily life in Korea (visa, healthcare, banking, transportation, housing, culture).
Formatting constraints: Do NOT use decorative characters or formatting such as '*', '#', triple backticks (```), raw HTML, emojis, ASCII art, tables, or code fences. Do NOT draw diagrams or maps.
Output rules: Reply in plain single-column text in the user's language. Keep answers brief and mobile-friendly (short paragraphs). If more detail is needed, provide a compact plain-text summary and ask whether to send extended information."""
}
