package com.sangamesh.fashionstore.data

import com.sangamesh.fashionstore.data.repository.MockProductRepository
import com.sangamesh.fashionstore.domain.model.AppError
import com.sangamesh.fashionstore.domain.model.AppResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MockProductRepositoryTest {
    private val repo = MockProductRepository()

    @Test fun `has at least 15 products and 6 categories`() = runBlocking {
        val products = (repo.getProducts() as AppResult.Success).data
        val categories = (repo.getCategories() as AppResult.Success).data
        assertTrue(products.size >= 15)
        assertEquals(6, categories.size)
    }

    @Test fun `category product counts match products`() = runBlocking {
        val products = (repo.getProducts() as AppResult.Success).data
        val categories = (repo.getCategories() as AppResult.Success).data
        categories.forEach { c -> assertEquals(products.count { it.category == c.name }, c.productCount) }
    }

    @Test fun `discount percentage is consistent with prices`() = runBlocking {
        val products = (repo.getProducts() as AppResult.Success).data
        products.forEach {
            assertEquals((it.originalPrice - it.price) * 100 / it.originalPrice, it.discountPercentage)
        }
    }

    @Test fun `unknown product returns 404`() = runBlocking {
        val result = repo.getProduct(-1)
        assertEquals(AppResult.Error(AppError.Http(404)), result)
    }
}
