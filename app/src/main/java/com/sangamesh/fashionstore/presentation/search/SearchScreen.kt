package com.sangamesh.fashionstore.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.sangamesh.fashionstore.presentation.common.ErrorView
import com.sangamesh.fashionstore.presentation.common.LoadingView
import com.sangamesh.fashionstore.presentation.common.ProductCard
import com.sangamesh.fashionstore.presentation.common.StoreSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onProductClick: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    StoreSearchBar(query = query, onQueryChange = viewModel::onQueryChange, onSearch = viewModel::search)
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            when (val s = state) {
                SearchUiState.Idle -> EmptyView("Search Fashion Store", "Try \"Dress\", \"Jeans\" or a brand name.")
                SearchUiState.Loading -> LoadingView()
                is SearchUiState.Error -> ErrorView(s.message, onRetry = viewModel::search)
                is SearchUiState.Results -> {
                    Text(
                        "${s.products.size} results for \"${s.query}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                    )
                    if (s.products.isEmpty()) {
                        EmptyView("No products found", "Try searching for \"Dress\", \"Jeans\" or \"Shirt\".")
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(s.products, key = { it.id }) { product ->
                                ProductCard(product, onClick = { onProductClick(product.id) }, onFavoriteClick = { viewModel.toggleFavorite(product.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}
