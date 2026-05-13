package com.example.booktracker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.data.model.Book
import com.example.booktracker.data.model.BookShelf
import com.example.booktracker.data.model.DictionaryCard
import com.example.booktracker.data.remote.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val bookRepository = ServiceLocator.bookRepository
    private val cardRepository = ServiceLocator.cardRepository
    private val authRepository = ServiceLocator.authRepository

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    val readingBooks: StateFlow<List<Book>> = _books
        .map { list -> list.filter { it.bookShelf == BookShelf.READING } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _cardsByBook = MutableStateFlow<Map<Long, List<DictionaryCard>>>(emptyMap())

    init {
        // Слушаем флоу логина: вошли — грузим книги текущего юзера, вышли — чистим state.
        // Так при смене аккаунта данные всегда соответствуют действующему JWT.
        viewModelScope.launch {
            authRepository.isLoggedInFlow.collect { loggedIn ->
                if (loggedIn) {
                    refreshBooks()
                } else {
                    _books.value = emptyList()
                    _cardsByBook.value = emptyMap()
                }
            }
        }
    }

    fun refreshBooks() {
        viewModelScope.launch {
            runCatching { bookRepository.getAll() }
                .onSuccess { _books.value = it }
                .onFailure { Log.e(TAG, "getAll books failed", it) }
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            runCatching { bookRepository.create(book) }
                .onSuccess { created -> _books.value = _books.value + created }
                .onFailure { Log.e(TAG, "create book failed", it) }
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            runCatching { bookRepository.update(book) }
                .onSuccess { updated ->
                    _books.value = _books.value.map { if (it.id == updated.id) updated else it }
                }
                .onFailure { Log.e(TAG, "update book failed", it) }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            runCatching { bookRepository.delete(book.id) }
                .onSuccess {
                    _books.value = _books.value.filter { it.id != book.id }
                    _cardsByBook.value = _cardsByBook.value - book.id
                }
                .onFailure { Log.e(TAG, "delete book failed", it) }
        }
    }

    /** StateFlow карточек для книги. При первом обращении грузит из сети. */
    fun cardsFor(bookId: Long): StateFlow<List<DictionaryCard>> {
        if (!_cardsByBook.value.containsKey(bookId)) {
            loadCards(bookId)
        }
        return _cardsByBook
            .map { it[bookId].orEmpty() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    }

    private fun loadCards(bookId: Long) {
        viewModelScope.launch {
            runCatching { cardRepository.getByBook(bookId) }
                .onSuccess { list -> _cardsByBook.value = _cardsByBook.value + (bookId to list) }
                .onFailure { Log.e(TAG, "getCards($bookId) failed", it) }
        }
    }

    fun addCard(bookId: Long, term: String, definition: String, context: String? = null) {
        val trimmedTerm = term.trim()
        val trimmedDefinition = definition.trim()
        if (trimmedTerm.isEmpty() || trimmedDefinition.isEmpty()) return

        viewModelScope.launch {
            runCatching {
                cardRepository.add(bookId, trimmedTerm, trimmedDefinition, context?.trim()?.ifBlank { null })
            }
                .onSuccess { created ->
                    val current = _cardsByBook.value[bookId].orEmpty()
                    _cardsByBook.value = _cardsByBook.value + (bookId to (listOf(created) + current))
                }
                .onFailure { Log.e(TAG, "addCard failed", it) }
        }
    }

    fun deleteCard(card: DictionaryCard) {
        viewModelScope.launch {
            runCatching { cardRepository.delete(card.bookId, card.id) }
                .onSuccess {
                    val current = _cardsByBook.value[card.bookId].orEmpty()
                    _cardsByBook.value = _cardsByBook.value + (card.bookId to current.filter { it.id != card.id })
                }
                .onFailure { Log.e(TAG, "deleteCard failed", it) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Чистка state делается автоматически наблюдателем isLoggedInFlow в init.
            authRepository.logout()
        }
    }

    private companion object {
        const val TAG = "MainViewModel"
    }
}
