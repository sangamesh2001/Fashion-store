package com.sangamesh.fashionstore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Order items and address are stored as JSON — simplest durable shape for a small local order history. */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String,
    val itemsJson: String,
    val subtotal: Int,
    val discount: Int,
    val deliveryFee: Int,
    val totalAmount: Int,
    val addressJson: String,
    val paymentMethod: String,
    val paymentStatus: String,
    val orderStatus: String,
    val createdAt: Long,
)
