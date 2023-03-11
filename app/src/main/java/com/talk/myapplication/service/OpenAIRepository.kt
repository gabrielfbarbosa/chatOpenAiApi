package com.talk.myapplication.service

class OpenAIRepository (
    private val openAIService: OpenAIService
) {
    suspend fun getResponse(apiKey: String, prompt: String): OpenAIResponse {
        val request = OpenAIRequest(
            "gpt-3.5-turbo",
            listOf(Messages("user", prompt))
        )
        return openAIService.getResponse(
            apiKey = "Bearer $apiKey",
            requestBody = request
        )
    }
}