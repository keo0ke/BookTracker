package com.example.booktracker.data.repository

import com.example.booktracker.data.model.ProgressEntry
import com.example.booktracker.data.remote.BookTrackerApi
import com.example.booktracker.data.remote.dto.ProgressRequestDto
import com.example.booktracker.data.remote.dto.ProgressResponseDto

class ProgressRepository(private val api: BookTrackerApi) {

    suspend fun record(bookId: Long, page: Int): ProgressEntry =
        api.recordProgress(bookId, ProgressRequestDto(page)).toDomain()

    suspend fun getAll(): List<ProgressEntry> =
        api.getProgress().map { it.toDomain() }
}

private fun ProgressResponseDto.toDomain(): ProgressEntry = ProgressEntry(
    id = id,
    bookId = bookId,
    bookTitle = bookTitle.orEmpty(),
    bookPageCount = pageCount,
    page = page,
    recordedAt = recordedAt.orEmpty(),
)
