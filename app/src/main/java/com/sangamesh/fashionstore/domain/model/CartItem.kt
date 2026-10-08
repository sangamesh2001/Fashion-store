package com.sangamesh.fashionstore.domain.model

data class CartItem(
    val id: Long = 0,
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

data class CartTotals(
    val subtotal: Int,
    val discount: Int,
    val deliveryFee: Int,
    val total: Int,
)
