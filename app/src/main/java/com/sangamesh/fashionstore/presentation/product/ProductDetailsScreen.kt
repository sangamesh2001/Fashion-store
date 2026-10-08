package com.sangamesh.fashionstore.presentation.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.presentation.common.ErrorView
import com.sangamesh.fashionstore.presentation.common.LoadingView
import com.sangamesh.fashionstore.presentation.common.PriceView
import com.sangamesh.fashionstore.presentation.common.PrimaryButton
import com.sangamesh.fashionstore.presentation.common.ProductImageCarousel
import com.sangamesh.fashionstore.presentation.common.QuantitySelector
import com.sangamesh.fashionstore.presentation.common.RatingView
import com.sangamesh.fashionstore.presentation.common.SecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    onBack: () -> Unit,
    onGoToCart: () -> Unit,
    viewModel: ProductDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    val message by viewModel.addToCartMessage.collectAsStateWithLifecycle()
    val added by viewModel.addedToCart.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let { snackbarHostState.showSnackbar(it); viewModel.consumeMessage() }
    }
    LaunchedEffect(added) {
        if (added) snackbarHostState.showSnackbar("Added to cart")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = viewModel::toggleFavorite) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from wishlist" else "Add to wishlist",
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        when (val s = state) {
            ProductDetailsUiState.Loading -> LoadingView(Modifier.padding(padding))
            is ProductDetailsUiState.Error -> ErrorView(s.message, onRetry = viewModel::load, modifier = Modifier.padding(padding))
            is ProductDetailsUiState.Success -> ProductDetailsContent(
                product = s.product,
                viewModel = viewModel,
                modifier = Modifier.padding(padding),
                onGoToCart = onGoToCart,
            )
        }
    }
}

@Composable
private fun ProductDetailsContent(
    product: Product,
    viewModel: ProductDetailsViewModel,
    modifier: Modifier = Modifier,
    onGoToCart: () -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            ProductImageCarousel(product.images, contentDescription = product.name)

            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(product.brand, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(product.name, style = MaterialTheme.typography.titleLarge)
                RatingView(product.rating, product.reviewCount)
                PriceView(product.price, product.originalPrice, product.discountPercentage)

                Text(
                    if (product.stock > 0) "In stock (${product.stock} available)" else "Out of stock",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (product.stock > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                )

                if (product.availableSizes.isNotEmpty()) {
                    OptionSection("Size", product.availableSizes, viewModel.selectedSize) { viewModel.selectSize(it) }
                }
                if (product.availableColors.isNotEmpty()) {
                    OptionSection("Color", product.availableColors, viewModel.selectedColor) { viewModel.selectColor(it) }
                }

                Text("Quantity", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                QuantitySelector(
                    quantity = viewModel.quantity,
                    onIncrement = { viewModel.updateQuantity(viewModel.quantity + 1) },
                    onDecrement = { viewModel.updateQuantity(viewModel.quantity - 1) },
                    maxQuantity = product.stock.coerceAtLeast(1),
                )

                Text("Description", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 12.dp))
                Text(product.description, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SecondaryButton(
                "Add to Cart",
                onClick = { viewModel.addToCart() },
                enabled = product.stock > 0,
                modifier = Modifier.weight(1f),
            )
            PrimaryButton(
                "Buy Now",
                onClick = { if (viewModel.addToCart()) onGoToCart() },
                enabled = product.stock > 0,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun OptionSection(label: String, options: List<String>, selected: String?, onSelect: (String) -> Unit) {
    Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                FilterChip(selected = option == selected, onClick = { onSelect(option) }, label = { Text(option) })
            }
        }
    }
}
