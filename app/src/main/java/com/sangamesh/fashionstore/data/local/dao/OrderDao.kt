package com.sangamesh.fashionstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sangamesh.fashionstore.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun observeOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrder(id: Long): OrderEntity?

    @Insert
    suspend fun insert(entity: OrderEntity): Long
}
