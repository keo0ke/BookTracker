package com.example.booktracker.data.remote

import android.content.Context
import com.example.booktracker.data.local.TokenStore
import com.example.booktracker.data.repository.AuthRepository
import com.example.booktracker.data.repository.BookRepository
import com.example.booktracker.data.repository.CardRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Простой ServiceLocator. Заменяет полноценный DI (Hilt) на ранней стадии.
 *
 * ВАЖНО: BASE_URL должен указывать на твой бэкенд.
 *  - Эмулятор Android: "http://10.0.2.2:8080/"
 *  - Реальный телефон в той же Wi-Fi сети: "http://<IPv4-адрес-ПК>:8080/"
 *    (узнать в Windows: cmd → ipconfig → IPv4 Address у Wi-Fi адаптера)
 */
object ServiceLocator {

    // IP компьютера в локальной Wi-Fi сети. Если IP сменится — поменяй тут.
    // Эмулятор Android вместо этого использует "http://10.0.2.2:8080/".
    private const val BASE_URL = "http://192.168.1.137:8080/"

    @Volatile private var initialized = false
    private lateinit var appContext: Context

    lateinit var tokenStore: TokenStore
        private set
    lateinit var api: BookTrackerApi
        private set
    lateinit var authRepository: AuthRepository
        private set
    lateinit var bookRepository: BookRepository
        private set
    lateinit var cardRepository: CardRepository
        private set

    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            appContext = context.applicationContext
            tokenStore = TokenStore(appContext)

            val json = Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(tokenStore))
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

            api = retrofit.create(BookTrackerApi::class.java)

            bookRepository = BookRepository(api)
            cardRepository = CardRepository(api)
            authRepository = AuthRepository(api, tokenStore)

            initialized = true
        }
    }
}
