package com.sangamesh.fashionstore.presentation.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.domain.model.SortOption
import com.sangamesh.fashionstore.presentation.common.EmptyView
import com.sangamesh.fashionstore.presentation.common.ErrorView
import com.sangamesh.fashionstore.presentation.common.FilterBottomSheet
import com.sangamesh.fashionstore.presentation.common.LoadingView
import com.sangamesh.fashionstore.presentation.common.ProductCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListingScreen(
    title: String,
    onBack: () -> Unit,
    onProductClick: (Long) -> Unit,
    viewModel: ProductListingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()

    var showFilters by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { showFilters = true }) { Icon(Icons.Default.Tune, "Filters") }
                },
            )
        },
    ) { padding ->
        when (val s = state) {
            ProductListingUiState.Loading -> LoadingView(Modifier.padding(padding))
            is ProductListingUiState.Error -> ErrorView(s.message, onRetry = viewModel::load, modifier = Modifier.padding(padding))
            is ProductListingUiState.Success -> Box(Modifier.padding(padding)) {
                androidx.compose.foundation.layout.Column {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "${s.products.size} results",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Box {
                            TextButton(onClick = { showSortMenu = true }) { Text("Sort: ${sort.label}") }
                            DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                                SortOption.entries.forEach { option ->
                                    DropdownMenuItem(text = { Text(option.label) }, onClick = { viewModel.updateSort(option); showSortMenu = false })
                                }
                            }
                        }
                    }

                    if (s.products.isEmpty()) {
                        EmptyView(
                            "No products found",
                            "Try adjusting your filters, or search for \"Dress\", \"Jeans\", \"Shirt\".",
                        )
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

        if (showFilters) {
            FilterBottomSheet(
                sheetState = sheetState,
                filters = filters,
                onApply = {
                    viewModel.updateFilters(it)
                    scope.launch { sheetState.hide() }.invokeOnCompletion { showFilters = false }
                },
                onDismiss = { scope.launch { sheetState.hide() }.invokeOnCompletion { showFilters = false } },
            )
        }
    }
}
