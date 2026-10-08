package com.sangamesh.fashionstore.domain.repository

import com.sangamesh.fashionstore.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(): Flow<List<CartItem>>
    suspend fun addToCart(item: CartItem)
    suspend fun updateQuantity(cartItemId: Long, quantity: Int)
    suspend fun remove(cartItemId: Long)
    suspend fun clear()
}
