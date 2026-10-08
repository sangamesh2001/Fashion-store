package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.AppError
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.HomeContent
import com.sangamesh.fashionstore.domain.repository.ProductRepository
import javax.inject.Inject

class GetHomeContentUseCase @Inject constructor(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(): AppResult<HomeContent> {
        val products = when (val r = repository.getProducts()) {
            is AppResult.Success -> r.data
            is AppResult.Error -> return r
        }
        val categories = when (val r = repository.getCategories()) {
            is AppResult.Success -> r.data
            is AppResult.Error -> return r
        }
        if (products.isEmpty()) return AppResult.Error(AppError.Empty)

        return AppResult.Success(
            HomeContent(
                categories = categories,
                featured = products.filter { it.rating >= 4.5f }.take(6),
                newArrivals = products.sortedByDescending { it.id }.take(6),
                bestSellers = products.sortedByDescending { it.reviewCount }.take(6),
            )
        )
    }
}
