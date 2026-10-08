package com.sangamesh.fashionstore.data.repository

import com.sangamesh.fashionstore.data.local.dao.CartDao
import com.sangamesh.fashionstore.data.local.entity.CartEntity
import com.sangamesh.fashionstore.domain.model.CartItem
import com.sangamesh.fashionstore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val dao: CartDao,
) : CartRepository {

    override fun observeCart(): Flow<List<CartItem>> = dao.observeCart().map { list -> list.map { it.toDomain() } }

    override suspend fun addToCart(item: CartItem) {
        val existing = dao.findMatching(item.productId, item.size, item.color)
        if (existing != null) {
            dao.updateQuantity(existing.id, (existing.quantity + item.quantity).coerceAtMost(item.stock.coerceAtLeast(1)))
        } else {
            dao.insert(item.toEntity())
        }
    }

    override suspend fun updateQuantity(cartItemId: Long, quantity: Int) {
        if (quantity <= 0) dao.delete(cartItemId) else dao.updateQuantity(cartItemId, quantity)
    }

    override suspend fun remove(cartItemId: Long) = dao.delete(cartItemId)

    override suspend fun clear() = dao.clear()
}

private fun CartEntity.toDomain() = CartItem(
    id = id, productId = productId, productName = productName, imageUrl = imageUrl,
    brand = brand, price = price, originalPrice = originalPrice, quantity = quantity,
    size = size, color = color, stock = stock,
)

private fun CartItem.toEntity() = CartEntity(
    id = id, productId = productId, productName = productName, imageUrl = imageUrl,
    brand = brand, price = price, originalPrice = originalPrice, quantity = quantity,
    size = size, color = color, stock = stock,
)
