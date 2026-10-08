package com.sangamesh.fashionstore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sangamesh.fashionstore.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun observeCart(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId AND size IS :size AND color IS :color LIMIT 1")
    suspend fun findMatching(productId: Long, size: String?, color: String?): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CartEntity): Long

    @Update
    suspend fun update(entity: CartEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM cart_items")
    suspend fun clear()
}
