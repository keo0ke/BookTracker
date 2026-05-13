package com.example.booktracker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookRequestDto(
    val title: String,
    val author: String? = null,
    val description: String? = null,
    val shelf: String? = null,
    val pageCount: Int? = null,
    val currentPage: Int? = null,
    val isbn: String? = null,
    val coverUri: String? = null,
)

@Serializable
data class BookResponseDto(
    val id: Long,
    val title: String,
    val author: String? = null,
    val description: String? = null,
    val shelf: String? = null,
    val pageCount: Int? = null,
    val currentPage: Int? = null,
    val isbn: String? = null,
    val coverUri: String? = null,
    val createdAt: String? = null,
)
