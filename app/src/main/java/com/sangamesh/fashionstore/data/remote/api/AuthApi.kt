package com.sangamesh.fashionstore.data.remote.api

import com.sangamesh.fashionstore.data.remote.dto.AuthResponse
import com.sangamesh.fashionstore.data.remote.dto.LoginRequest
import com.sangamesh.fashionstore.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}
