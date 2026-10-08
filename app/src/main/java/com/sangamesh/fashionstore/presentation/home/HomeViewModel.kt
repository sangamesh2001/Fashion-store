package com.sangamesh.fashionstore.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.WishlistItem
import com.sangamesh.fashionstore.domain.usecase.GetHomeContentUseCase
import com.sangamesh.fashionstore.domain.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeContent: GetHomeContentUseCase,
    private val wishlistRepository: WishlistRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.value = HomeUiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = getHomeContent()) {
                is AppResult.Success -> HomeUiState.Success(result.data)
                is AppResult.Error -> HomeUiState.Error(result.error.userMessage())
            }
        }
    }

    fun toggleFavorite(productId: Long) {
        val content = (state.value as? HomeUiState.Success)?.content ?: return
        val product = (content.featured + content.newArrivals + content.bestSellers).firstOrNull { it.id == productId } ?: return
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
}
