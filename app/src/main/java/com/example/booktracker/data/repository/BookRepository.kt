package com.example.booktracker.data.repository

import com.example.booktracker.data.model.Book
import com.example.booktracker.data.remote.BookTrackerApi
import com.example.booktracker.data.remote.dto.BookRequestDto
import com.example.booktracker.data.remote.dto.BookResponseDto

class BookRepository(private val api: BookTrackerApi) {

    suspend fun getAll(): List<Book> = api.getBooks().map { it.toDomain() }

    suspend fun create(book: Book): Book =
        api.createBook(book.toRequestDto()).toDomain()

    suspend fun update(book: Book): Book =
        api.updateBook(book.id, book.toRequestDto()).toDomain()

    suspend fun delete(id: Long) = api.deleteBook(id)
}

private fun Book.toRequestDto(): BookRequestDto = BookRequestDto(
    title = title,
    author = author.ifBlank { null },
    description = description,
    shelf = shelf,
    pageCount = pageCount,
    currentPage = currentPage,
    isbn = isbn,
    coverUri = coverUri,
)

private fun BookResponseDto.toDomain(): Book = Book(
    id = id,
    title = title,
    author = author.orEmpty(),
    description = description,
    shelf = shelf ?: "WISH",
    pageCount = pageCount,
    currentPage = currentPage ?: 0,
    isbn = isbn,
    coverUri = coverUri,
)
