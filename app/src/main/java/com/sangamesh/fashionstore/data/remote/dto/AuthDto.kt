package com.sangamesh.fashionstore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
)

@Serializable
data class AuthResponse(val token: String, val userId: Long, val fullName: String, val email: String, val phoneNumber: String)
