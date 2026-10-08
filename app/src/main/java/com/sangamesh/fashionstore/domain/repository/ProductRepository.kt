package com.sangamesh.fashionstore.domain.repository

import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.Category
import com.sangamesh.fashionstore.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): AppResult<List<Product>>
    suspend fun getProduct(id: Long): AppResult<Product>
    suspend fun getCategories(): AppResult<List<Category>>
}
