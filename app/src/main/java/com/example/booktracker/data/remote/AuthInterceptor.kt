package com.example.booktracker.data.remote

import com.example.booktracker.data.local.TokenStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Перехватчик, который перед каждым запросом добавляет JWT-токен в Authorization-заголовок.
 * На auth-эндпоинты (register/authenticate) токен не клеится — там его ещё нет.
 */
class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        val needsAuth = !path.contains("/api/v1/auth/")
        val token = if (needsAuth) runBlocking { tokenStore.getToken() } else null

        val finalRequest = if (token != null) {
            request.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else {
            request
        }

        val response = chain.proceed(finalRequest)

        // Если сервер сказал что токен невалиден — чистим его, фронт автоматом откроет AuthScreen.
        if (needsAuth && (response.code == 401 || response.code == 403)) {
            runBlocking { tokenStore.clear() }
        }

        return response
    }
}
