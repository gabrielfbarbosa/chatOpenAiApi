package com.talk.myapplication.service

import com.google.gson.annotations.SerializedName

data class OpenAIRequest(
        @SerializedName("model") val model: String,
        @SerializedName("messages") val messages: List<Messages>
    )

    data class Messages(
        @SerializedName("role") var role: String,
        @SerializedName("content") var content: String
    )
