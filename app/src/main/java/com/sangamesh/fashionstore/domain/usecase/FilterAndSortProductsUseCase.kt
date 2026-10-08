package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.domain.model.ProductFilters
import com.sangamesh.fashionstore.domain.model.SortOption
import javax.inject.Inject

class FilterAndSortProductsUseCase @Inject constructor() {

    operator fun invoke(
        products: List<Product>,
        filters: ProductFilters,
        sort: SortOption,
        query: String = "",
    ): List<Product> {
        var result = products

        if (query.isNotBlank()) {
            val q = query.trim()
            result = result.filter {
                it.name.contains(q, ignoreCase = true) ||
                    it.brand.contains(q, ignoreCase = true) ||
                    it.category.contains(q, ignoreCase = true)
            }
        }

        if (filters.categories.isNotEmpty()) {
            result = result.filter { it.category in filters.categories }
        }
        if (filters.priceRanges.isNotEmpty()) {
            result = result.filter { product ->
                filters.priceRanges.any { range ->
                    product.price >= range.min && (range.max == null || product.price < range.max)
                }
            }
        }
        if (filters.sizes.isNotEmpty()) {
            result = result.filter { product -> product.availableSizes.any { it in filters.sizes } }
        }
        if (filters.colors.isNotEmpty()) {
            result = result.filter { product -> product.availableColors.any { it in filters.colors } }
        }
        filters.minRating?.let { min -> result = result.filter { it.rating >= min } }

        return when (sort) {
            SortOption.RELEVANCE -> result
            SortOption.PRICE_LOW_TO_HIGH -> result.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> result.sortedByDescending { it.price }
            SortOption.RATING -> result.sortedByDescending { it.rating }
            SortOption.NEWEST -> result.sortedByDescending { it.id }
            SortOption.DISCOUNT -> result.sortedByDescending { it.discountPercentage }
        }
    }
}
