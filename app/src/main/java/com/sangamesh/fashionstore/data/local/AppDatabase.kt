package com.sangamesh.fashionstore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sangamesh.fashionstore.data.local.dao.AddressDao
import com.sangamesh.fashionstore.data.local.dao.CartDao
import com.sangamesh.fashionstore.data.local.dao.OrderDao
import com.sangamesh.fashionstore.data.local.dao.WishlistDao
import com.sangamesh.fashionstore.data.local.entity.AddressEntity
import com.sangamesh.fashionstore.data.local.entity.CartEntity
import com.sangamesh.fashionstore.data.local.entity.OrderEntity
import com.sangamesh.fashionstore.data.local.entity.WishlistEntity

@Database(
    entities = [CartEntity::class, WishlistEntity::class, AddressEntity::class, OrderEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun addressDao(): AddressDao
    abstract fun orderDao(): OrderDao
}
