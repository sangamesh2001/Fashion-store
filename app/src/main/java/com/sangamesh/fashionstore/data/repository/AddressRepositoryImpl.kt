package com.sangamesh.fashionstore.data.repository

import com.sangamesh.fashionstore.data.local.dao.AddressDao
import com.sangamesh.fashionstore.data.local.entity.AddressEntity
import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepositoryImpl @Inject constructor(
    private val dao: AddressDao,
) : AddressRepository {

    override fun observeAddresses(): Flow<List<Address>> = dao.observeAddresses().map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(address: Address): Long {
        val isFirstAddress = address.id == 0L
        val id = dao.upsert(address.toEntity())
        if (address.isDefault) dao.markDefault(id) else if (isFirstAddress) dao.markDefault(id)
        return id
    }

    override suspend fun delete(addressId: Long) = dao.delete(addressId)

    override suspend fun setDefault(addressId: Long) {
        dao.clearDefault()
        dao.markDefault(addressId)
    }
}

private fun AddressEntity.toDomain() = Address(
    id = id, fullName = fullName, phoneNumber = phoneNumber, addressLine1 = addressLine1,
    addressLine2 = addressLine2, city = city, state = state, postalCode = postalCode,
    country = country, isDefault = isDefault,
)

private fun Address.toEntity() = AddressEntity(
    id = id, fullName = fullName, phoneNumber = phoneNumber, addressLine1 = addressLine1,
    addressLine2 = addressLine2, city = city, state = state, postalCode = postalCode,
    country = country, isDefault = isDefault,
)
