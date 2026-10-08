package com.sangamesh.fashionstore.domain.repository

import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.RegisterInfo
import com.sangamesh.fashionstore.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isLoggedIn(): Flow<Boolean>
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun register(info: RegisterInfo): AppResult<User>
    suspend fun logout()
    suspend fun currentUser(): User?
}
