package com.sangamesh.fashionstore.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.CartItem
import com.sangamesh.fashionstore.domain.model.CartTotals
import com.sangamesh.fashionstore.domain.repository.CartRepository
import com.sangamesh.fashionstore.domain.usecase.CalculateCartTotalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartUiState(val items: List<CartItem> = emptyList(), val totals: CartTotals = CartTotals(0, 0, 0, 0))

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val calculateTotals: CalculateCartTotalsUseCase,
) : ViewModel() {

    val state: StateFlow<CartUiState> = cartRepository.observeCart()
        .map { items -> CartUiState(items, calculateTotals(items)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartUiState())

    fun increment(item: CartItem) = updateQuantity(item, item.quantity + 1)
    fun decrement(item: CartItem) = updateQuantity(item, item.quantity - 1)

    private fun updateQuantity(item: CartItem, quantity: Int) {
        if (quantity > item.stock) return
        viewModelScope.launch { cartRepository.updateQuantity(item.id, quantity) }
    }

    fun remove(item: CartItem) {
        viewModelScope.launch { cartRepository.remove(item.id) }
    }
}
