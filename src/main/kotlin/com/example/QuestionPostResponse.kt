package com.example

import kotlinx.serialization.Serializable

@Serializable
data class QuestionPostResponse(
    val response: String
)