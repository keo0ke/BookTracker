package com.example.booktracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    val message: String? = null,
    val timestamp: String? = null,
)
