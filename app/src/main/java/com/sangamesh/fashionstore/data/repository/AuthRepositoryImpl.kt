package com.sangamesh.fashionstore.data.repository

import com.sangamesh.fashionstore.domain.model.AppError
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.RegisterInfo
import com.sangamesh.fashionstore.domain.model.User
import com.sangamesh.fashionstore.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock auth so the app is fully usable before the backend exists.
 * Ready to be swapped for a Retrofit-backed implementation that calls AuthApi (see data/remote/api/AuthApi.kt).
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val session: SessionManager,
) : AuthRepository {

    override fun isLoggedIn(): Flow<Boolean> = session.isLoggedIn()

    override suspend fun login(email: String, password: String): AppResult<User> {
        delay(NETWORK_DELAY_MS)
        if (password.isBlank()) return AppResult.Error(AppError.Http(401))
        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        val userId = email.hashCode().toLong()
        session.saveSession(token = fakeToken(), userId = userId, name = name, email = email, phone = "")
        return AppResult.Success(User(userId, name, email, ""))
    }

    override suspend fun register(info: RegisterInfo): AppResult<User> {
        delay(NETWORK_DELAY_MS)
        val userId = info.email.hashCode().toLong()
        session.saveSession(
            token = fakeToken(), userId = userId,
            name = info.fullName, email = info.email, phone = info.phoneNumber,
        )
        return AppResult.Success(User(userId, info.fullName, info.email, info.phoneNumber))
    }

    override suspend fun logout() = session.clearSession()

    override suspend fun currentUser(): User? {
        val id = session.userId().first() ?: return null
        return User(
            id = id,
            fullName = session.userName().first().orEmpty(),
            email = session.userEmail().first().orEmpty(),
            phoneNumber = session.userPhone().first().orEmpty(),
        )
    }

    private fun fakeToken() = "mock-" + UUID.randomUUID()

    companion object {
        private const val NETWORK_DELAY_MS = 500L
    }
}
