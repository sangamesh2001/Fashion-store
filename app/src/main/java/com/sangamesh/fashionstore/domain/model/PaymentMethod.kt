package com.sangamesh.fashionstore.domain.model

enum class PaymentMethod(val displayName: String) {
    CASH_ON_DELIVERY("Cash on Delivery"),
    MOCK_CARD("Mock Card Payment"),
    MOCK_UPI("Mock UPI Payment"),
}

enum class PaymentStatus { PENDING, PAID, FAILED }
