package com.sangamesh.fashionstore.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.domain.model.SortOption
import com.sangamesh.fashionstore.domain.model.WishlistItem
import com.sangamesh.fashionstore.domain.repository.ProductRepository
import com.sangamesh.fashionstore.domain.repository.WishlistRepository
import com.sangamesh.fashionstore.domain.usecase.FilterAndSortProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Results(val query: String, val products: List<Product>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val filterAndSort: FilterAndSortProductsUseCase,
    private val wishlistRepository: WishlistRepository,
) : ViewModel() {

    private var allProducts: List<Product>? = null

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _state = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    fun onQueryChange(value: String) { _query.value = value }

    fun search() {
        val q = _query.value
        if (q.isBlank()) { _state.value = SearchUiState.Idle; return }

        val cached = allProducts
        if (cached != null) {
            _state.value = SearchUiState.Results(q, filterAndSort(cached, com.sangamesh.fashionstore.domain.model.ProductFilters(), SortOption.RELEVANCE, q))
            return
        }

        _state.value = SearchUiState.Loading
        viewModelScope.launch {
            when (val result = repository.getProducts()) {
                is AppResult.Success -> {
                    allProducts = result.data
                    _state.value = SearchUiState.Results(q, filterAndSort(result.data, com.sangamesh.fashionstore.domain.model.ProductFilters(), SortOption.RELEVANCE, q))
                }
                is AppResult.Error -> _state.value = SearchUiState.Error(result.error.userMessage())
            }
        }
    }

    fun toggleFavorite(productId: Long) {
        val product = allProducts?.firstOrNull { it.id == productId } ?: return
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
