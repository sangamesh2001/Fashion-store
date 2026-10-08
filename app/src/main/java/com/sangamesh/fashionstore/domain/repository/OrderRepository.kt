package com.sangamesh.fashionstore.domain.repository

import com.sangamesh.fashionstore.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun observeOrders(): Flow<List<Order>>
    suspend fun getOrder(orderId: Long): Order?
    suspend fun placeOrder(order: Order): Long
}
