package com.example

import kotlinx.serialization.Serializable

@Serializable
data class QuestionPostRequest(
    val message: String
)