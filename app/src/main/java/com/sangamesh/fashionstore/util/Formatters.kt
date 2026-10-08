package com.sangamesh.fashionstore.util

import java.text.NumberFormat
import java.util.Locale

private val inr: NumberFormat = NumberFormat.getNumberInstance(Locale("en", "IN"))

fun formatPrice(amount: Int): String = "₹" + inr.format(amount)
