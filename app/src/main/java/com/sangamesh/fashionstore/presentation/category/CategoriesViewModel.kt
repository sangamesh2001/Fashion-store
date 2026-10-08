package com.sangamesh.fashionstore.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.AppResult
import com.sangamesh.fashionstore.domain.model.Category
import com.sangamesh.fashionstore.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CategoriesUiState {
    data object Loading : CategoriesUiState
    data class Success(val categories: List<Category>) : CategoriesUiState
    data class Error(val message: String) : CategoriesUiState
}

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val state: StateFlow<CategoriesUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        _state.value = CategoriesUiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = repository.getCategories()) {
                is AppResult.Success -> CategoriesUiState.Success(result.data)
                is AppResult.Error -> CategoriesUiState.Error(result.error.userMessage())
            }
        }
    }
}
