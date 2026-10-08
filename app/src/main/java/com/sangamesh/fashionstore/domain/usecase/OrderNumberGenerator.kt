package com.sangamesh.fashionstore.domain.usecase

import javax.inject.Inject

class OrderNumberGenerator @Inject constructor() {
    operator fun invoke(): String = "FS" + (100000..999999).random()
}
