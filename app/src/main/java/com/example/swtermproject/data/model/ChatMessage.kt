package com.example.swtermproject.data.model

data class ChatMessage(
    val role: String,
    val content: String
)

data class OpenAiRequest(
    val model: String,
    val messages: List<ChatMessage>
)

data class OpenAiResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)
