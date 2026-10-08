package com.sangamesh.fashionstore.domain.model

data class HomeContent(
    val categories: List<Category>,
    val featured: List<Product>,
    val newArrivals: List<Product>,
    val bestSellers: List<Product>,
)
