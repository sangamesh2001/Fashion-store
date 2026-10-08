package com.sangamesh.fashionstore.domain.repository

import com.sangamesh.fashionstore.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun observeAddresses(): Flow<List<Address>>
    suspend fun upsert(address: Address): Long
    suspend fun delete(addressId: Long)
    suspend fun setDefault(addressId: Long)
}
