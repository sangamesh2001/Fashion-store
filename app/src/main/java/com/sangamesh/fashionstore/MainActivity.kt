package com.sangamesh.fashionstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.presentation.navigation.AppViewModel
import com.sangamesh.fashionstore.presentation.navigation.FashionStoreNavHost
import com.sangamesh.fashionstore.ui.theme.FashionStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = hiltViewModel()
            val darkModePref by appViewModel.isDarkMode.collectAsStateWithLifecycle()
            val cartCount by appViewModel.cartItemCount.collectAsStateWithLifecycle()

            FashionStoreTheme(darkTheme = darkModePref ?: androidx.compose.foundation.isSystemInDarkTheme()) {
                FashionStoreNavHost(cartCount = cartCount)
            }
        }
    }
}
