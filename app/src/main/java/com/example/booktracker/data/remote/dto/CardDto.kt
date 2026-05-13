package com.example.booktracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardRequestDto(
    val term: String,
    val definition: String? = null,
    val context: String? = null,
)

@Serializable
data class CardResponseDto(
    val id: Long,
    val term: String,
    val definition: String? = null,
    val context: String? = null,
    val createdAt: String? = null,
)
