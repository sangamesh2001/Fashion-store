package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.domain.model.CartItem
import javax.inject.Inject

sealed interface CheckoutValidation {
    data object Valid : CheckoutValidation
    data class Invalid(val reason: String) : CheckoutValidation
}

class ValidateCheckoutUseCase @Inject constructor() {
    operator fun invoke(items: List<CartItem>, address: Address?): CheckoutValidation = when {
        items.isEmpty() -> CheckoutValidation.Invalid("Your cart is empty.")
        address == null -> CheckoutValidation.Invalid("Please select a delivery address.")
        items.any { it.quantity > it.stock } -> CheckoutValidation.Invalid("Some items exceed available stock.")
        else -> CheckoutValidation.Valid
    }
}
