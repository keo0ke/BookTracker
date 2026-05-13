package com.example.booktracker.data.model

enum class BookShelf {
    READING,
    FINISHED,
    WISH,
}

/**
 * Доменная модель книги. id = 0 для ещё не сохранённой на сервере книги.
 */
data class Book(
    val id: Long = 0L,
    val title: String,
    val author: String,
    val isbn: String? = null,
    val coverUri: String? = null,
    val pageCount: Int? = null,
    val currentPage: Int = 0,
    val description: String? = null,
    val shelf: String = BookShelf.WISH.name,
) {
    val bookShelf: BookShelf
        get() = try {
            BookShelf.valueOf(shelf)
        } catch (_: IllegalArgumentException) {
            BookShelf.WISH
        }
}
