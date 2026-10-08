package com.sangamesh.fashionstore.data.repository

import com.sangamesh.fashionstore.data.local.dao.WishlistDao
import com.sangamesh.fashionstore.data.local.entity.WishlistEntity
import com.sangamesh.fashionstore.domain.model.WishlistItem
import com.sangamesh.fashionstore.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepositoryImpl @Inject constructor(
    private val dao: WishlistDao,
) : WishlistRepository {

    override fun observeWishlist(): Flow<List<WishlistItem>> =
        dao.observeWishlist().map { list -> list.map { it.toDomain() } }

    override fun isFavorite(productId: Long): Flow<Boolean> = dao.isFavorite(productId)

    override suspend fun toggle(item: WishlistItem) {
        val currentlyFavorite = dao.isFavorite(item.productId).first()
        if (currentlyFavorite) dao.deleteByProductId(item.productId) else dao.insert(item.toEntity())
    }
}

private fun WishlistEntity.toDomain() = WishlistItem(
    productId = productId, productName = productName, imageUrl = imageUrl, brand = brand,
    price = price, originalPrice = originalPrice, discountPercentage = discountPercentage,
    rating = rating, reviewCount = reviewCount,
)

private fun WishlistItem.toEntity() = WishlistEntity(
    productId = productId, productName = productName, imageUrl = imageUrl, brand = brand,
    price = price, originalPrice = originalPrice, discountPercentage = discountPercentage,
    rating = rating, reviewCount = reviewCount,
)
