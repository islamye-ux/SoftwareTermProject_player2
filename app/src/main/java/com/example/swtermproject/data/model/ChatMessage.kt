package com.example.swtermproject.data.model

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    val role: String,
    val content: String
)

// ── Gemini REST API models ──────────────────────────────────────
data class GeminiPart(val text: String)

data class GeminiContent(
    val role: String,
    val parts: List<GeminiPart>
)

data class GeminiSystemInstruction(
    val parts: List<GeminiPart>
)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    @SerializedName("system_instruction")
    val systemInstruction: GeminiSystemInstruction? = null
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>
)

data class GeminiCandidate(
    val content: GeminiContent
)
