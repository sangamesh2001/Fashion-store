package com.sangamesh.fashionstore.domain.model

import kotlinx.serialization.Serializable

/** @Serializable so it can be persisted as JSON inside an OrderEntity (see OrderRepositoryImpl). */
@Serializable
data class Address(
    val id: Long = 0,
    val fullName: String,
    val phoneNumber: String,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String,
    val isDefault: Boolean = false,
) {
    fun formatted(): String = buildString {
        append(addressLine1)
        if (addressLine2.isNotBlank()) append(", $addressLine2")
        append(", $city, $state $postalCode, $country")
    }
}
