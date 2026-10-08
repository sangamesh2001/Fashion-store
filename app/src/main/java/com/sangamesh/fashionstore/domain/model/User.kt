package com.sangamesh.fashionstore.domain.model

data class User(
    val id: Long,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
)

data class RegisterInfo(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
)
