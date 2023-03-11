package com.talk.myapplication.service

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

interface OpenAIService {

    @POST("v1/chat/completions")
    suspend fun getResponse(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") apiKey: String,
        @Body requestBody: OpenAIRequest
    ): OpenAIResponse
}