package com.example.booktracker.data.remote

import com.example.booktracker.data.remote.dto.AuthRequestDto
import com.example.booktracker.data.remote.dto.AuthResponseDto
import com.example.booktracker.data.remote.dto.BookRequestDto
import com.example.booktracker.data.remote.dto.BookResponseDto
import com.example.booktracker.data.remote.dto.CardRequestDto
import com.example.booktracker.data.remote.dto.CardResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookTrackerApi {

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: AuthRequestDto): AuthResponseDto

    @POST("api/v1/auth/authenticate")
    suspend fun authenticate(@Body request: AuthRequestDto): AuthResponseDto

    @GET("api/v1/books")
    suspend fun getBooks(): List<BookResponseDto>

    @POST("api/v1/books")
    suspend fun createBook(@Body request: BookRequestDto): BookResponseDto

    @PUT("api/v1/books/{id}")
    suspend fun updateBook(
        @Path("id") id: Long,
        @Body request: BookRequestDto,
    ): BookResponseDto

    @DELETE("api/v1/books/{id}")
    suspend fun deleteBook(@Path("id") id: Long)

    @GET("api/v1/books/{bookId}/cards")
    suspend fun getCards(@Path("bookId") bookId: Long): List<CardResponseDto>

    @POST("api/v1/books/{bookId}/cards")
    suspend fun addCard(
        @Path("bookId") bookId: Long,
        @Body request: CardRequestDto,
    ): CardResponseDto

    @DELETE("api/v1/books/{bookId}/cards/{cardId}")
    suspend fun deleteCard(
        @Path("bookId") bookId: Long,
        @Path("cardId") cardId: Long,
    )
}
