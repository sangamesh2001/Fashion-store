package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.PriceRange
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.domain.model.ProductFilters
import com.sangamesh.fashionstore.domain.model.SortOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterAndSortProductsUseCaseTest {
    private val useCase = FilterAndSortProductsUseCase()

    private fun product(
        id: Long, name: String, brand: String = "Brand", category: String = "Dresses",
        price: Int = 1000, original: Int = 1000, rating: Float = 4f,
        sizes: List<String> = listOf("M"), colors: List<String> = listOf("Black"),
    ) = Product(
        id = id, name = name, description = "", price = price, originalPrice = original,
        discountPercentage = if (original > 0) (original - price) * 100 / original else 0,
        brand = brand, category = category, images = emptyList(),
        availableSizes = sizes, availableColors = colors, rating = rating, reviewCount = 10, stock = 5,
    )

    private val products = listOf(
        product(1, "Red Dress", category = "Dresses", price = 1500, rating = 4.5f),
        product(2, "Blue Jeans", category = "Jeans", price = 2500, rating = 4.0f),
        product(3, "White Shirt", category = "Shirts", price = 500, rating = 3.5f),
    )

    @Test fun `search query matches name brand or category case-insensitively`() {
        val result = useCase(products, ProductFilters(), SortOption.RELEVANCE, query = "dress")
        assertEquals(1, result.size)
        assertEquals(1L, result.first().id)
    }

    @Test fun `category filter narrows results`() {
        val result = useCase(products, ProductFilters(categories = setOf("Jeans")), SortOption.RELEVANCE)
        assertEquals(1, result.size)
        assertEquals("Jeans", result.first().category)
    }

    @Test fun `price range filter includes only matching products`() {
        val result = useCase(products, ProductFilters(priceRanges = setOf(PriceRange("Under 1000", 0, 1000))), SortOption.RELEVANCE)
        assertTrue(result.all { it.price < 1000 })
    }

    @Test fun `sort by price low to high orders ascending`() {
        val result = useCase(products, ProductFilters(), SortOption.PRICE_LOW_TO_HIGH)
        assertEquals(listOf(500, 1500, 2500), result.map { it.price })
    }

    @Test fun `sort by rating orders descending`() {
        val result = useCase(products, ProductFilters(), SortOption.RATING)
        assertEquals(listOf(4.5f, 4.0f, 3.5f), result.map { it.rating })
    }

    @Test fun `no matches returns empty list`() {
        val result = useCase(products, ProductFilters(), SortOption.RELEVANCE, query = "nonexistent")
        assertTrue(result.isEmpty())
    }
}
