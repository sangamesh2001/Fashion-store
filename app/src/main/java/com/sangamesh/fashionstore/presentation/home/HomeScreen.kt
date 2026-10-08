package com.sangamesh.fashionstore.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.domain.model.Category
import com.sangamesh.fashionstore.domain.model.HomeContent
import com.sangamesh.fashionstore.domain.model.Product
import com.sangamesh.fashionstore.presentation.common.ErrorView
import com.sangamesh.fashionstore.presentation.common.LoadingView
import com.sangamesh.fashionstore.presentation.common.ProductCard
import com.sangamesh.fashionstore.presentation.common.StoreSearchBar
import com.sangamesh.fashionstore.ui.theme.RoseLight

@Composable
fun HomeScreen(
    onProductClick: (Long) -> Unit,
    onCategoryClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (val s = state) {
        HomeUiState.Loading -> LoadingView()
        is HomeUiState.Error -> ErrorView(s.message, onRetry = viewModel::load)
        is HomeUiState.Success -> HomeContentView(s.content, onProductClick, onCategoryClick, viewModel::toggleFavorite, onSearchClick, onProfileClick, onCartClick)
    }
}

@Composable
private fun HomeContentView(
    content: HomeContent,
    onProductClick: (Long) -> Unit,
    onCategoryClick: (String) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item { HomeHeader(onProfileClick, onCartClick) }
        item {
            Box(Modifier.padding(horizontal = 16.dp)) {
                StoreSearchBar(query = "", onQueryChange = {}, onSearch = onSearchClick, readOnly = true)
                // Overlay to make the whole field act as a single tap-through into Search, per the "large search field" spec.
                Box(Modifier.fillMaxWidth().padding(top = 0.dp).clickable0(onSearchClick))
            }
        }
        item { PromoBanner() }
        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(content.categories, key = { it.id }) { category ->
                    AssistChip(onClick = { onCategoryClick(category.name) }, label = { Text(category.name) })
                }
            }
        }
        item { SectionTitle("Featured Products") }
        item { ProductRow(content.featured, onProductClick, onToggleFavorite) }
        item { SectionTitle("New Arrivals") }
        productGrid(content.newArrivals, onProductClick, onToggleFavorite)
        item { SectionTitle("Best Sellers") }
        productGrid(content.bestSellers, onProductClick, onToggleFavorite)
    }
}

@Composable
private fun HomeHeader(onProfileClick: () -> Unit, onCartClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text("Hello 👋", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Fashion Store", style = MaterialTheme.typography.headlineMedium)
        }
        Row {
            IconButton(onClick = onProfileClick) { Icon(Icons.Default.Person, contentDescription = "Profile") }
            IconButton(onClick = onCartClick) { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") }
        }
    }
}

@Composable
private fun PromoBanner() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .aspectRatio(16f / 7f)
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, RoseLight))),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.Center) {
            Text("SUMMER COLLECTION", color = Color.White, style = MaterialTheme.typography.labelSmall)
            Text("UP TO 40% OFF", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text("SHOP NOW", color = Color.White, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 6.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) =
    Text(text, Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.titleLarge)

@Composable
private fun ProductRow(products: List<Product>, onProductClick: (Long) -> Unit, onToggleFavorite: (Long) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(products, key = { it.id }) {
            ProductCard(it, onClick = { onProductClick(it.id) }, onFavoriteClick = { onToggleFavorite(it.id) }, modifier = Modifier.width(170.dp))
        }
    }
}

/** Two-column grid built from rows, since LazyVerticalGrid can't nest inside LazyColumn. */
private fun androidx.compose.foundation.lazy.LazyListScope.productGrid(
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
) {
    items(products.chunked(2), key = { row -> row.first().id }) { row ->
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            row.forEach { ProductCard(it, onClick = { onProductClick(it.id) }, onFavoriteClick = { onToggleFavorite(it.id) }, modifier = Modifier.weight(1f)) }
            if (row.size == 1) Spacer(Modifier.weight(1f))
        }
    }
}

private fun Modifier.clickable0(onClick: () -> Unit) = this.then(Modifier.clickable(onClick = onClick))
