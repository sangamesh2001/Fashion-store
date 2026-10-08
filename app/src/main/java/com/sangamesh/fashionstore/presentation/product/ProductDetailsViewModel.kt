package com.sangamesh.fashionstore.presentation.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.CartItem
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.domain.model.WishlistItem
import com.sangamesh.fashionstore.domain.repository.CartRepository
import com.sangamesh.fashionstore.domain.repository.ProductRepository
import com.sangamesh.fashionstore.domain.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductDetailsUiState {
    data object Loading : ProductDetailsUiState
    data class Success(val product: Product) : ProductDetailsUiState
    data class Error(val message: String) : ProductDetailsUiState
}

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val productId: Long = checkNotNull(savedStateHandle.get<Long>("id"))

    private val _state = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val state: StateFlow<ProductDetailsUiState> = _state.asStateFlow()

    val isFavorite: StateFlow<Boolean> = wishlistRepository.isFavorite(productId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    var selectedSize: String? by mutableStateOf(null)
        private set
    var selectedColor: String? by mutableStateOf(null)
        private set
    var quantity: Int by mutableIntStateOf(1)
        private set

    private val _addToCartMessage = MutableStateFlow<String?>(null)
    val addToCartMessage: StateFlow<String?> = _addToCartMessage.asStateFlow()

    private val _addedToCart = MutableStateFlow(false)
    val addedToCart: StateFlow<Boolean> = _addedToCart.asStateFlow()

    init { load() }

    fun load() {
        _state.value = ProductDetailsUiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = productRepository.getProduct(productId)) {
                is AppResult.Success -> ProductDetailsUiState.Success(result.data)
                is AppResult.Error -> ProductDetailsUiState.Error(result.error.userMessage())
            }
        }
    }

    fun selectSize(size: String) { selectedSize = size }
    fun selectColor(color: String) { selectedColor = color }
    fun updateQuantity(qty: Int) { quantity = qty.coerceAtLeast(1) }

    fun toggleFavorite() {
        val product = (state.value as? ProductDetailsUiState.Success)?.product ?: return
        viewModelScope.launch {
            wishlistRepository.toggle(
                WishlistItem(
                    productId = product.id, productName = product.name, imageUrl = product.images.firstOrNull().orEmpty(),
                    brand = product.brand, price = product.price, originalPrice = product.originalPrice,
                    discountPercentage = product.discountPercentage, rating = product.rating, reviewCount = product.reviewCount,
                ),
            )
        }
    }

    /** Returns false (and a message) when a required size/color hasn't been chosen. */
    fun addToCart(): Boolean {
        val product = (state.value as? ProductDetailsUiState.Success)?.product ?: return false
        if (product.availableSizes.isNotEmpty() && selectedSize == null) {
            _addToCartMessage.value = "Please select a size"
            return false
        }
        if (product.availableColors.isNotEmpty() && selectedColor == null) {
            _addToCartMessage.value = "Please select a color"
            return false
        }
        viewModelScope.launch {
            cartRepository.addToCart(
                CartItem(
                    productId = product.id, productName = product.name, imageUrl = product.images.firstOrNull().orEmpty(),
                    brand = product.brand, price = product.price, originalPrice = product.originalPrice,
                    quantity = quantity, size = selectedSize, color = selectedColor, stock = product.stock,
                ),
            )
            _addedToCart.value = true
        }
        return true
    }

    fun consumeMessage() { _addToCartMessage.value = null }
}
