package com.talk.myapplication.service

import com.google.gson.annotations.SerializedName

data class OpenAIResponse(
        @SerializedName("id") val id: String,
        @SerializedName("object") val `object`: String,
        @SerializedName("created") val created: Long,
        @SerializedName("model") val model: String,
        @SerializedName("usage") val usage: Usage,
        @SerializedName("choices") val choices: List<OpenAIChoice>
    )

data class Usage(
        @SerializedName("prompt_tokens")val prompt_tokens: Int,
        @SerializedName("completion_tokens")val completion_tokens: Int,
        @SerializedName("total_tokens")val total_tokens: Int,
    )

data class OpenAIChoice(
        @SerializedName("message") val message: Message,
        @SerializedName("finish_reason") val finish_reason: String,
        @SerializedName("index") val index: Int
    )

data class Message(
        @SerializedName("role") val role: String,
        @SerializedName("content") val content: String
    )
