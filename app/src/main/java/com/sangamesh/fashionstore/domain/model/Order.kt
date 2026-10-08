package com.sangamesh.fashionstore.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val productId: Long,
    val productName: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int,
    val size: String?,
    val color: String?,
)

data class Order(
    val id: Long = 0,
    val orderNumber: String,
    val items: List<OrderItem>,
    val subtotal: Int,
    val discount: Int,
    val deliveryFee: Int,
    val totalAmount: Int,
    val address: Address,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val orderStatus: OrderStatus,
    val createdAt: Long,
) {
    val itemCount: Int get() = items.sumOf { it.quantity }
}
