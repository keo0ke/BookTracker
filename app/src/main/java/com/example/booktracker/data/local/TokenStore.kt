package com.example.booktracker.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.tokenDataStore by preferencesDataStore(name = "auth_prefs")

class TokenStore(private val context: Context) {

    val tokenFlow: Flow<String?> = context.tokenDataStore.data.map { it[TOKEN_KEY] }

    suspend fun getToken(): String? = tokenFlow.first()

    suspend fun saveToken(token: String) {
        context.tokenDataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun clear() {
        context.tokenDataStore.edit { it.remove(TOKEN_KEY) }
    }

    private companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }
}
