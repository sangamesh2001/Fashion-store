package com.sangamesh.fashionstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sangamesh.fashionstore.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM addresses ORDER BY isDefault DESC, id DESC")
    fun observeAddresses(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: AddressEntity): Long

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE addresses SET isDefault = 0")
    suspend fun clearDefault()

    @Query("UPDATE addresses SET isDefault = 1 WHERE id = :id")
    suspend fun markDefault(id: Long)
}
