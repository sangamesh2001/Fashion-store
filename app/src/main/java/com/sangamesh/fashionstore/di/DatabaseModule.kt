package com.sangamesh.fashionstore.di

import android.content.Context
import androidx.room.Room
import com.sangamesh.fashionstore.data.local.AppDatabase
import com.sangamesh.fashionstore.data.local.dao.AddressDao
import com.sangamesh.fashionstore.data.local.dao.CartDao
import com.sangamesh.fashionstore.data.local.dao.OrderDao
import com.sangamesh.fashionstore.data.local.dao.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "fashion_store.db").build()

    @Provides fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()
    @Provides fun provideWishlistDao(db: AppDatabase): WishlistDao = db.wishlistDao()
    @Provides fun provideAddressDao(db: AppDatabase): AddressDao = db.addressDao()
    @Provides fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()
}
