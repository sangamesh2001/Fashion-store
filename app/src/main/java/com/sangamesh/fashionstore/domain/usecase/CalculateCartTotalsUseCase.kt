package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.CartItem
import com.sangamesh.fashionstore.domain.model.CartTotals
import javax.inject.Inject

/** Pure calculation, kept out of the ViewModel so it's directly unit-testable. */
class CalculateCartTotalsUseCase @Inject constructor() {

    operator fun invoke(items: List<CartItem>): CartTotals {
        if (items.isEmpty()) return CartTotals(0, 0, 0, 0)

        val subtotal = items.sumOf { it.price * it.quantity }
        val discount = items.sumOf { (it.originalPrice - it.price).coerceAtLeast(0) * it.quantity }
        val deliveryFee = if (subtotal >= FREE_DELIVERY_THRESHOLD) 0 else STANDARD_DELIVERY_FEE
        val total = subtotal + deliveryFee

        return CartTotals(subtotal = subtotal, discount = discount, deliveryFee = deliveryFee, total = total)
    }

    companion object {
        const val STANDARD_DELIVERY_FEE = 50
        const val FREE_DELIVERY_THRESHOLD = 5000
    }
}
