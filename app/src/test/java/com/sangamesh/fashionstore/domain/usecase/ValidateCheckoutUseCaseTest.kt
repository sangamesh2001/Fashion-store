package com.sangamesh.fashionstore.domain.usecase

import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.domain.model.CartItem
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateCheckoutUseCaseTest {
    private val useCase = ValidateCheckoutUseCase()

    private val address = Address(
        fullName = "Sangamesh", phoneNumber = "9999999999", addressLine1 = "1 Main St",
        addressLine2 = "", city = "Bengaluru", state = "KA", postalCode = "560001", country = "India",
    )

    private fun item(quantity: Int = 1, stock: Int = 5) = CartItem(
        id = 1, productId = 1, productName = "Test", imageUrl = "", brand = "B",
        price = 100, originalPrice = 100, quantity = quantity, size = null, color = null, stock = stock,
    )

    @Test fun `empty cart is invalid`() {
        assertTrue(useCase(emptyList(), address) is CheckoutValidation.Invalid)
    }

    @Test fun `missing address is invalid`() {
        assertTrue(useCase(listOf(item()), null) is CheckoutValidation.Invalid)
    }

    @Test fun `quantity exceeding stock is invalid`() {
        assertTrue(useCase(listOf(item(quantity = 10, stock = 5)), address) is CheckoutValidation.Invalid)
    }

    @Test fun `valid cart and address passes`() {
        assertTrue(useCase(listOf(item()), address) is CheckoutValidation.Valid)
    }
}
