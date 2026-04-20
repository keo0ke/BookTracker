package com.example.booktracker.data.model

import java.util.UUID

enum class BookShelf {
    READING,
    FINISHED,
    WISH,
}

data class Book(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val isbn: String? = null,
    val coverUri: String? = null,
    val pageCount: Int? = null,
    val description: String? = null,
    val shelf: BookShelf = BookShelf.WISH,
    val createdAt: Long = System.currentTimeMillis(),
)
