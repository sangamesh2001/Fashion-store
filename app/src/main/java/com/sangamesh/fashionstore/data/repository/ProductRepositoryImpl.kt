package com.sangamesh.fashionstore.data.repository

import com.sangamesh.fashionstore.data.mapper.toDomain
import com.sangamesh.fashionstore.data.remote.api.ProductApi
import com.sangamesh.fashionstore.domain.model.AppError
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.Category
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.domain.repository.ProductRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real backend-backed implementation, ready for when the Spring Boot API is live.
 * Swap this in for MockProductRepository in di/RepositoryModule.kt — no UI or ViewModel changes needed.
 */
@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
) : ProductRepository {

    override suspend fun getProducts(): AppResult<List<Product>> = safeCall {
        api.getProducts().map { it.toDomain() }
    }

    override suspend fun getProduct(id: Long): AppResult<Product> = safeCall {
        api.getProduct(id).toDomain()
    }

    override suspend fun getCategories(): AppResult<List<Category>> = safeCall {
        api.getCategories().map { it.toDomain() }
    }

    private inline fun <T> safeCall(block: () -> T): AppResult<T> = try {
        AppResult.Success(block())
    } catch (e: SocketTimeoutException) {
        AppResult.Error(AppError.Timeout)
    } catch (e: HttpException) {
        AppResult.Error(AppError.Http(e.code()))
    } catch (e: IOException) {
        AppResult.Error(AppError.NoNetwork)
    } catch (e: Exception) {
        AppResult.Error(AppError.Unknown)
    }
}
