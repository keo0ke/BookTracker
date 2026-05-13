package com.example.booktracker.data.model

/**
 * Доменная модель словарной карточки.
 */
data class DictionaryCard(
    val id: Long = 0L,
    val bookId: Long,
    val term: String,
    val definition: String,
    val context: String? = null,
)
