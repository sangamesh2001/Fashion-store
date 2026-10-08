package com.sangamesh.fashionstore.domain.model

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
}

/** Centralised error types; raw exceptions never reach the UI. */
sealed interface AppError {
    data object NoNetwork : AppError
    data object Timeout : AppError
    data class Http(val code: Int) : AppError
    data object InvalidResponse : AppError
    data object Empty : AppError
    data object InvalidCredentials : AppError
    data object EmailAlreadyRegistered : AppError
    data object Unknown : AppError

    fun userMessage(): String = when (this) {
        NoNetwork -> "No internet connection. Please check your network."
        Timeout -> "The request timed out. Please try again."
        is Http -> when (code) {
            400 -> "That request wasn't valid. Please check and try again."
            401, 403 -> "You need to sign in again."
            404 -> "We couldn't find what you were looking for."
            in 500..599 -> "Our servers are having trouble. Please try later."
            else -> "Something went wrong."
        }
        InvalidResponse -> "We received an unexpected response."
        Empty -> "Nothing to show yet."
        InvalidCredentials -> "Invalid email or password."
        EmailAlreadyRegistered -> "An account with this email already exists."
        Unknown -> "Something went wrong."
    }
}
