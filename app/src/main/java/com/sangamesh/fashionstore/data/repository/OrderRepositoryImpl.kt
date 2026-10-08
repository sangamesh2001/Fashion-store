package com.sangamesh.fashionstore.data.repository

import com.sangamesh.fashionstore.data.local.dao.OrderDao
import com.sangamesh.fashionstore.data.local.entity.OrderEntity
import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.domain.model.Order
import com.sangamesh.fashionstore.domain.model.OrderItem
import com.sangamesh.fashionstore.domain.model.OrderStatus
import com.sangamesh.fashionstore.domain.model.PaymentMethod
import com.sangamesh.fashionstore.domain.model.PaymentStatus
import com.sangamesh.fashionstore.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val dao: OrderDao,
) : OrderRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeOrders(): Flow<List<Order>> = dao.observeOrders().map { list -> list.map { it.toDomain() } }

    override suspend fun getOrder(orderId: Long): Order? = dao.getOrder(orderId)?.toDomain()

    override suspend fun placeOrder(order: Order): Long = dao.insert(order.toEntity())

    private fun OrderEntity.toDomain() = Order(
        id = id,
        orderNumber = orderNumber,
        items = json.decodeFromString(itemsJson),
        subtotal = subtotal, discount = discount, deliveryFee = deliveryFee, totalAmount = totalAmount,
        address = json.decodeFromString(addressJson),
        paymentMethod = PaymentMethod.valueOf(paymentMethod),
        paymentStatus = PaymentStatus.valueOf(paymentStatus),
        orderStatus = OrderStatus.valueOf(orderStatus),
        createdAt = createdAt,
    )

    private fun Order.toEntity() = OrderEntity(
        id = id,
        orderNumber = orderNumber,
        itemsJson = json.encodeToString(items),
        subtotal = subtotal, discount = discount, deliveryFee = deliveryFee, totalAmount = totalAmount,
        addressJson = json.encodeToString(address),
        paymentMethod = paymentMethod.name,
        paymentStatus = paymentStatus.name,
        orderStatus = orderStatus.name,
        createdAt = createdAt,
    )
}
