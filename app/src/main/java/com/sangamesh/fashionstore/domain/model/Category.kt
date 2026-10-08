package com.sangamesh.fashionstore.domain.model

data class Category(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val productCount: Int,
)
