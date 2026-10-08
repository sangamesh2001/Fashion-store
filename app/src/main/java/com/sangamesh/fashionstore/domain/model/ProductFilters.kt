package com.sangamesh.fashionstore.domain.model

data class PriceRange(val label: String, val min: Int, val max: Int?)

val priceRanges = listOf(
    PriceRange("Under ₹500", 0, 500),
    PriceRange("₹500–₹1,000", 500, 1000),
    PriceRange("₹1,000–₹2,000", 1000, 2000),
    PriceRange("₹2,000+", 2000, null),
)

data class ProductFilters(
    val categories: Set<String> = emptySet(),
    val priceRanges: Set<PriceRange> = emptySet(),
    val sizes: Set<String> = emptySet(),
    val colors: Set<String> = emptySet(),
    val minRating: Float? = null,
) {
    val isEmpty: Boolean
        get() = categories.isEmpty() && priceRanges.isEmpty() && sizes.isEmpty() &&
            colors.isEmpty() && minRating == null
}

enum class SortOption(val label: String) {
    RELEVANCE("Relevance"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    RATING("Rating"),
    NEWEST("Newest"),
    DISCOUNT("Discount"),
}
