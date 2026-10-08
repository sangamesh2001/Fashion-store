package com.sangamesh.fashionstore.domain.model

enum class OrderStatus(val label: String) {
    PLACED("Order Placed"),
    CONFIRMED("Confirmed"),
    PACKED("Packed"),
    SHIPPED("Shipped"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
}
