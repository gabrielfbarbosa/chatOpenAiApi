package com.talk.myapplication.service

import android.hardware.biometrics.BiometricPrompt

data class OpenAiPayload(
        val prompt: String,
        val isUserMessage: Boolean
    )

