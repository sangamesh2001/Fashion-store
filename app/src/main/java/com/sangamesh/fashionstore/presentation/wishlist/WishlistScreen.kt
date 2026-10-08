package com.sangamesh.fashionstore.presentation.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.presentation.common.EmptyView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    onProductClick: (Long) -> Unit,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    val items by viewModel.items.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Wishlist", style = MaterialTheme.typography.titleLarge) }) }) { padding ->
        if (items.isEmpty()) {
            EmptyView("Your wishlist is empty", "Start adding products you love.", modifier = Modifier.padding(padding))
            return@Scaffold
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items, key = { it.productId }) { item ->
                com.sangamesh.fashionstore.presentation.common.ProductCard(
                    product = item.toProductForCard(),
                    onClick = { onProductClick(item.productId) },
                    onFavoriteClick = { viewModel.remove(item) },
                )
            }
        }
    }
}

private fun com.sangamesh.fashionstore.domain.model.WishlistItem.toProductForCard() =
    com.sangamesh.fashionstore.domain.model.Product(
        id = productId, name = productName, description = "", price = price, originalPrice = originalPrice,
        discountPercentage = discountPercentage, brand = brand, category = "", images = listOf(imageUrl),
        availableSizes = emptyList(), availableColors = emptyList(), rating = rating, reviewCount = reviewCount,
        stock = 1, isFavorite = true,
    )
