package com.talk.myapplication.service

import android.os.Message
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class OpenAiServer {
    companion object {
        private const val BASE_URL = "https://api.openai.com/v1/"
    }

    private val client = OkHttpClient()

    fun sendMessage(request: OpenAIRequest, apiKey: String): OpenAIResponse? {
        val url = "$BASE_URL/chat/completions"
        val mediaType = "application/json".toMediaType()

        val requestBody = Gson().toJson(request).toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).execute().use { response ->
            return if (response.isSuccessful) {
                Gson().fromJson(response.body?.string(), OpenAIResponse::class.java)
            } else {
                null
            }
        }
    }
}
