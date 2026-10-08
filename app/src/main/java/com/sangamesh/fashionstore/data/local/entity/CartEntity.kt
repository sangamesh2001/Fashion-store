package com.sangamesh.fashionstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: Long,
    val productName: String,
    val imageUrl: String,
    val brand: String,
    val price: Int,
    val originalPrice: Int,
    val quantity: Int,
    val size: String?,
    val color: String?,
    val stock: Int,
)
