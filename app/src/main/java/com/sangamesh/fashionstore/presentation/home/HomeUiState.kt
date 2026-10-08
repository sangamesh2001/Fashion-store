package com.sangamesh.fashionstore.presentation.home

import com.sangamesh.fashionstore.domain.model.HomeContent

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val content: HomeContent) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
