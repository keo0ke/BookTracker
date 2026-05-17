package com.example.booktracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProgressRequestDto(
    val page: Int,
)

@Serializable
data class ProgressResponseDto(
    val id: Long,
    val bookId: Long,
    val bookTitle: String? = null,
    val pageCount: Int? = null,
    val page: Int,
    val recordedAt: String? = null,
)
