package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.CartItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateCartTotalsUseCaseTest {
    private val useCase = CalculateCartTotalsUseCase()

    private fun item(price: Int, originalPrice: Int, quantity: Int) = CartItem(
        id = 1, productId = 1, productName = "Test", imageUrl = "", brand = "B",
        price = price, originalPrice = originalPrice, quantity = quantity,
        size = null, color = null, stock = 100,
    )

    @Test fun `empty cart totals are all zero`() {
        val totals = useCase(emptyList())
        assertEquals(0, totals.subtotal)
        assertEquals(0, totals.discount)
        assertEquals(0, totals.deliveryFee)
        assertEquals(0, totals.total)
    }

    @Test fun `subtotal discount and delivery match the example in the spec`() {
        // subtotal = 1999 x 2, discount = 500, delivery = 50 -> total = 3548
        val totals = useCase(listOf(item(price = 1999, originalPrice = 2249, quantity = 2)))
        assertEquals(3998, totals.subtotal)
        assertEquals(500, totals.discount)
        assertEquals(50, totals.deliveryFee)
        assertEquals(4048, totals.total)
    }

    @Test fun `delivery is free at or above the free-delivery threshold`() {
        val totals = useCase(listOf(item(price = 5000, originalPrice = 5000, quantity = 1)))
        assertEquals(0, totals.deliveryFee)
        assertEquals(5000, totals.total)
    }

    @Test fun `multiple items sum correctly`() {
        val totals = useCase(
            listOf(
                item(price = 500, originalPrice = 500, quantity = 2),
                item(price = 1000, originalPrice = 1200, quantity = 1),
            ),
        )
        assertEquals(2000, totals.subtotal)
        assertEquals(200, totals.discount)
    }
}
