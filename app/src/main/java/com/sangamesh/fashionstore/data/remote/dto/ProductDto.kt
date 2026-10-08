package com.sangamesh.fashionstore.data.remote.dto

import kotlinx.serialization.Serializable

/** Wire format for the future Spring Boot API. Mapped to the domain Product in ProductMapper. */
@Serializable
data class ProductDto(
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
)

@Serializable
data class CategoryDto(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val productCount: Int,
)
