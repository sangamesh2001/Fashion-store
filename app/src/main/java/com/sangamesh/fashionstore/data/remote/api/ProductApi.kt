package com.sangamesh.fashionstore.data.remote.api

import com.sangamesh.fashionstore.data.remote.dto.CategoryDto
import com.sangamesh.fashionstore.data.remote.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Matches the planned Spring Boot endpoints. Not called until ProductRepositoryImpl is bound. */
interface ProductApi {

    @GET("api/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Long): ProductDto

    @GET("api/products/search")
    suspend fun searchProducts(@Query("query") query: String): List<ProductDto>

    @GET("api/categories")
    suspend fun getCategories(): List<CategoryDto>
}
