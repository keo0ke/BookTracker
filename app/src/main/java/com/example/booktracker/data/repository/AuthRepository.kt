package com.example.booktracker.data.repository

import com.example.booktracker.data.local.TokenStore
import com.example.booktracker.data.remote.BookTrackerApi
import com.example.booktracker.data.remote.dto.AuthRequestDto
import com.example.booktracker.data.remote.dto.ErrorResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import retrofit2.HttpException

class AuthRepository(
    private val api: BookTrackerApi,
    private val tokenStore: TokenStore,
) {

    val isLoggedInFlow: Flow<Boolean> = tokenStore.tokenFlow.map { !it.isNullOrBlank() }

    suspend fun register(email: String, password: String): Result<Unit> = runCatchingHttp {
        val response = api.register(AuthRequestDto(email, password))
        tokenStore.saveToken(response.token)
    }

    suspend fun login(email: String, password: String): Result<Unit> = runCatchingHttp {
        val response = api.authenticate(AuthRequestDto(email, password))
        tokenStore.saveToken(response.token)
    }

    suspend fun logout() {
        tokenStore.clear()
    }

    /**
     * runCatching, который для HttpException достаёт человеческое сообщение из errorBody.
     */
    private inline fun runCatchingHttp(block: () -> Unit): Result<Unit> = runCatching(block)
        .recoverCatching { throwable ->
            if (throwable is HttpException) {
                val raw = throwable.response()?.errorBody()?.string().orEmpty()
                val parsed = runCatching {
                    JSON.decodeFromString(ErrorResponseDto.serializer(), raw)
                }.getOrNull()
                throw RuntimeException(parsed?.message?.ifBlank { null } ?: "Ошибка сервера: ${throwable.code()}")
            }
            throw throwable
        }

    private companion object {
        val JSON = Json { ignoreUnknownKeys = true; explicitNulls = false }
    }
}
