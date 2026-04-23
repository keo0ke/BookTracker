package com.example.booktracker.data.repository

import com.example.booktracker.data.local.BookDao
import com.example.booktracker.data.model.Book
import com.example.booktracker.data.model.BookShelf
import kotlinx.coroutines.flow.Flow

class BookRepository(private val dao: BookDao) {

    fun observeAllBooks(): Flow<List<Book>> = dao.observeAll()

    fun observeReadingBooks(): Flow<List<Book>> = dao.observeByShelf(BookShelf.READING)

    suspend fun getBook(id: String): Book? = dao.getById(id)

    suspend fun addBook(book: Book) = dao.insert(book)

    suspend fun updateBook(book: Book) = dao.update(book)

    suspend fun deleteBook(book: Book) = dao.delete(book)
}
