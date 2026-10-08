package com.sangamesh.fashionstore.data.mapper

import com.sangamesh.fashionstore.data.remote.dto.CategoryDto
import com.sangamesh.fashionstore.data.remote.dto.ProductDto
import com.sangamesh.fashionstore.domain.model.Category
import com.sangamesh.fashionstore.domain.model.Product

fun ProductDto.toDomain(): Product = Product(
    id = id, name = name, description = description, price = price,
    originalPrice = originalPrice, discountPercentage = discountPercentage,
    brand = brand, category = category, images = images,
    availableSizes = availableSizes, availableColors = availableColors,
    rating = rating, reviewCount = reviewCount, stock = stock,
)

fun CategoryDto.toDomain(): Category = Category(id = id, name = name, imageUrl = imageUrl, productCount = productCount)
