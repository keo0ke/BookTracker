package com.example.booktracker.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

enum class BookShelf {
    READING,
    FINISHED,
    WISH,
}

@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val author: String,
    val isbn: String? = null,
    val coverUri: String? = null,
    val pageCount: Int? = null,
    val currentPage: Int = 0,
    val description: String? = null,
    val shelf: String = BookShelf.WISH.name,
    val createdAt: Long = System.currentTimeMillis(),
) {
    @get:Ignore
    val bookShelf: BookShelf
        get() = try {
            BookShelf.valueOf(shelf)
        } catch (_: IllegalArgumentException) {
            BookShelf.WISH
        }
}
