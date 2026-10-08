package com.sangamesh.fashionstore.domain.model

data class WishlistItem(
    val productId: Long,
    val productName: String,
    val imageUrl: String,
    val brand: String,
    val price: Int,
    val originalPrice: Int,
    val discountPercentage: Int,
    val rating: Float,
    val reviewCount: Int,
)
