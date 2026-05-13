package com.example.booktracker.data.repository

import com.example.booktracker.data.model.DictionaryCard
import com.example.booktracker.data.remote.BookTrackerApi
import com.example.booktracker.data.remote.dto.CardRequestDto
import com.example.booktracker.data.remote.dto.CardResponseDto

class CardRepository(private val api: BookTrackerApi) {

    suspend fun getByBook(bookId: Long): List<DictionaryCard> =
        api.getCards(bookId).map { it.toDomain(bookId) }

    suspend fun add(bookId: Long, term: String, definition: String, context: String?): DictionaryCard =
        api.addCard(
            bookId,
            CardRequestDto(term = term, definition = definition, context = context),
        ).toDomain(bookId)

    suspend fun delete(bookId: Long, cardId: Long) = api.deleteCard(bookId, cardId)
}

private fun CardResponseDto.toDomain(bookId: Long): DictionaryCard = DictionaryCard(
    id = id,
    bookId = bookId,
    term = term,
    definition = definition.orEmpty(),
    context = context,
)
