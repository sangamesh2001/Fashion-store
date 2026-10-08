package com.sangamesh.fashionstore.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.domain.model.ProductFilters
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

sealed interface ProductListingUiState {
    data object Loading : ProductListingUiState
    data class Success(val products: List<Product>, val total: Int) : ProductListingUiState
    data class Error(val message: String) : ProductListingUiState
}

@HiltViewModel
class ProductListingViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val filterAndSort: FilterAndSortProductsUseCase,
    private val wishlistRepository: WishlistRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var allProducts: List<Product> = emptyList()
    private val initialCategory: String? = savedStateHandle.get<String>("category")

    private val _filters = MutableStateFlow(
        ProductFilters(categories = initialCategory?.let { setOf(it) } ?: emptySet()),
    )
    val filters: StateFlow<ProductFilters> = _filters.asStateFlow()

    private val _sort = MutableStateFlow(SortOption.RELEVANCE)
    val sort: StateFlow<SortOption> = _sort.asStateFlow()

    private val _state = MutableStateFlow<ProductListingUiState>(ProductListingUiState.Loading)
    val state: StateFlow<ProductListingUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.value = ProductListingUiState.Loading
        viewModelScope.launch {
            when (val result = repository.getProducts()) {
                is AppResult.Success -> {
                    allProducts = result.data
                    applyFilters()
                }
                is AppResult.Error -> _state.value = ProductListingUiState.Error(result.error.userMessage())
            }
        }
    }

    fun updateFilters(filters: ProductFilters) {
        _filters.value = filters
        applyFilters()
    }

    fun updateSort(sort: SortOption) {
        _sort.value = sort
        applyFilters()
    }

    private fun applyFilters() {
        val result = filterAndSort(allProducts, _filters.value, _sort.value)
        _state.value = ProductListingUiState.Success(result, allProducts.size)
    }

    fun toggleFavorite(productId: Long) {
        val product = allProducts.firstOrNull { it.id == productId } ?: return
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
