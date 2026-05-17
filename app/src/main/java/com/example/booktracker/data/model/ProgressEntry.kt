package com.example.booktracker.data.model

/**
 * Одна запись истории прогресса чтения.
 * recordedAt — ISO-строка времени с сервера (например "2026-05-17T19:30:45.123").
 */
data class ProgressEntry(
    val id: Long,
    val bookId: Long,
    val bookTitle: String,
    val bookPageCount: Int?,
    /** Абсолютная страница, на которой пользователь остановился. */
    val page: Int,
    val recordedAt: String,
)

