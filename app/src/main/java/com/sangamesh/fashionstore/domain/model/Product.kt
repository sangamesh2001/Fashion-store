package com.sangamesh.fashionstore.domain.model

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val originalPrice: Int,
    val discountPercentage: Int,
    val brand: String,
    val category: String,
    val images: List<String>,
    val availableSizes: List<String>,
    val availableColors: List<String>,
    val rating: Float,
    val reviewCount: Int,
    val stock: Int,
    val isFavorite: Boolean = false,
)
