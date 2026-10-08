package com.sangamesh.fashionstore.di

import com.sangamesh.fashionstore.data.repository.AddressRepositoryImpl
import com.sangamesh.fashionstore.data.repository.AuthRepositoryImpl
import com.sangamesh.fashionstore.data.repository.CartRepositoryImpl
import com.sangamesh.fashionstore.data.repository.MockProductRepository
import com.sangamesh.fashionstore.data.repository.OrderRepositoryImpl
import com.sangamesh.fashionstore.data.repository.SettingsRepositoryImpl
import com.sangamesh.fashionstore.data.repository.WishlistRepositoryImpl
import com.sangamesh.fashionstore.domain.repository.AddressRepository
import com.sangamesh.fashionstore.domain.repository.AuthRepository
import com.sangamesh.fashionstore.domain.repository.CartRepository
import com.sangamesh.fashionstore.domain.repository.OrderRepository
import com.sangamesh.fashionstore.domain.repository.ProductRepository
import com.sangamesh.fashionstore.domain.repository.SettingsRepository
import com.sangamesh.fashionstore.domain.repository.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /** Swap for ProductRepositoryImpl (data/repository/ProductRepositoryImpl.kt) once the Spring Boot API is live. */
    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: MockProductRepository): ProductRepository

    /** Swap for a Retrofit-backed implementation calling AuthApi once the backend is live. */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindWishlistRepository(impl: WishlistRepositoryImpl): WishlistRepository

    @Binds
    @Singleton
    abstract fun bindAddressRepository(impl: AddressRepositoryImpl): AddressRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
