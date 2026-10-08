package com.sangamesh.fashionstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey val productId: Long,
    val productName: String,
    val imageUrl: String,
    val brand: String,
    val price: Int,
    val originalPrice: Int,
    val discountPercentage: Int,
    val rating: Float,
    val reviewCount: Int,
)
