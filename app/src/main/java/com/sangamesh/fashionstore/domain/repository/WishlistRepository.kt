package com.sangamesh.fashionstore.domain.repository

import com.sangamesh.fashionstore.domain.model.WishlistItem
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun observeWishlist(): Flow<List<WishlistItem>>
    fun isFavorite(productId: Long): Flow<Boolean>
    suspend fun toggle(item: WishlistItem)
}
