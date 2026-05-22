package com.example.booktracker.data.remote

import android.content.Context
import android.os.Build
import com.example.booktracker.data.local.TokenStore
import com.example.booktracker.data.repository.AuthRepository
import com.example.booktracker.data.repository.BookRepository
import com.example.booktracker.data.repository.CardRepository
import com.example.booktracker.data.repository.ProgressRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Конфигурация сети для автоматического переключения между эмулятором и реальным устройством.
 */
object NetworkConfig {
    private val isEmulator: Boolean
        get() = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk_gphone")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator"))

    val baseUrl: String
        get() = if (isEmulator) {
            "http://10.0.2.2:8080/" // Для любой виртуалки
        } else {
            // ЕСЛИ НУЖНО ПРОВЕРИТЬ НА РЕАЛЬНОМ ТЕЛЕФОНЕ МЕНЯЕМ АЙПИ ПО Wi-Fi!!! НИЖЕ
            //Как найти Айпи? Для Win:
            // 1.Win+R
            // 2. Пишем cmd
            // 3. CMD пишем ipconfig
            // 4. Локальный адрес указан в строке IPv4-адрес
            // Вставляем его НИЖЕ!!!
            "http://192.168.1.137:8080/"
        }
}

object ServiceLocator {

    @Volatile private var initialized = false

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
    lateinit var progressRepository: ProgressRepository
        private set

    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            tokenStore = TokenStore(context.applicationContext)

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
                // Используем динамический URL из NetworkConfig
                .baseUrl(NetworkConfig.baseUrl)
                .client(client)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

            api = retrofit.create(BookTrackerApi::class.java)

            bookRepository = BookRepository(api)
            cardRepository = CardRepository(api)
            progressRepository = ProgressRepository(api)
            authRepository = AuthRepository(api, tokenStore)

            initialized = true
        }
    }
}