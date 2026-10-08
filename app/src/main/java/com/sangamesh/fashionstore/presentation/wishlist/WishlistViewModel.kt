package com.sangamesh.fashionstore.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.model.WishlistItem
import com.sangamesh.fashionstore.domain.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
) : ViewModel() {

    val items: StateFlow<List<WishlistItem>> = wishlistRepository.observeWishlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun remove(item: WishlistItem) {
        viewModelScope.launch { wishlistRepository.toggle(item) }
    }
}
