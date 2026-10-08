package com.sangamesh.fashionstore.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Session/auth preference, backed by DataStore per the "lightweight settings" guidance. Never stores the password. */
@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val TOKEN = stringPreferencesKey("auth_token")
        val USER_ID = longPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHONE = stringPreferencesKey("user_phone")
    }

    fun authToken(): Flow<String?> = dataStore.data.map { it[Keys.TOKEN] }
    fun isLoggedIn(): Flow<Boolean> = dataStore.data.map { !it[Keys.TOKEN].isNullOrBlank() }

    fun userId(): Flow<Long?> = dataStore.data.map { it[Keys.USER_ID] }
    fun userName(): Flow<String?> = dataStore.data.map { it[Keys.USER_NAME] }
    fun userEmail(): Flow<String?> = dataStore.data.map { it[Keys.USER_EMAIL] }
    fun userPhone(): Flow<String?> = dataStore.data.map { it[Keys.USER_PHONE] }

    suspend fun saveSession(token: String, userId: Long, name: String, email: String, phone: String) {
        dataStore.edit {
            it[Keys.TOKEN] = token
            it[Keys.USER_ID] = userId
            it[Keys.USER_NAME] = name
            it[Keys.USER_EMAIL] = email
            it[Keys.USER_PHONE] = phone
        }
    }

    suspend fun clearSession() {
        dataStore.edit {
            it.remove(Keys.TOKEN)
            it.remove(Keys.USER_ID)
            it.remove(Keys.USER_NAME)
            it.remove(Keys.USER_EMAIL)
            it.remove(Keys.USER_PHONE)
        }
    }
}
