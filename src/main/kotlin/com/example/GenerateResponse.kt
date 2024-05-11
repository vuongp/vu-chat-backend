package com.example

import kotlinx.serialization.Serializable

@Serializable
data class GenerateResponse(
    val response: String,
    val total_duration: Long
)