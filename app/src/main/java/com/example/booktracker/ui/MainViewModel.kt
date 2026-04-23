package com.example.booktracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.data.local.AppDatabase
import com.example.booktracker.data.model.Book
import com.example.booktracker.data.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookRepository

    val books: StateFlow<List<Book>>
    val readingBooks: StateFlow<List<Book>>

    init {
        val dao = AppDatabase.getInstance(application).bookDao()
        repository = BookRepository(dao)

        books = repository.observeAllBooks()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

        readingBooks = repository.observeReadingBooks()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    }

    fun addBook(book: Book) {
        viewModelScope.launch { repository.addBook(book) }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch { repository.updateBook(book) }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch { repository.deleteBook(book) }
    }
}
