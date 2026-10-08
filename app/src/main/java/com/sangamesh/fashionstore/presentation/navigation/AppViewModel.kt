package com.sangamesh.fashionstore.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangamesh.fashionstore.domain.repository.CartRepository
import com.sangamesh.fashionstore.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** Shared, app-wide bits used both by the theme (MainActivity) and the bottom nav (cart badge). */
@HiltViewModel
class AppViewModel @Inject constructor(
    cartRepository: CartRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    val cartItemCount: StateFlow<Int> = cartRepository.observeCart()
        .map { items -> items.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val isDarkMode: StateFlow<Boolean?> = settingsRepository.isDarkMode()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
