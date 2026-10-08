package com.sangamesh.fashionstore.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sangamesh.fashionstore.domain.model.PriceRange
import com.sangamesh.fashionstore.domain.model.ProductFilters
import com.sangamesh.fashionstore.domain.model.priceRanges

private val allSizes = listOf("S", "M", "L", "XL", "XXL")
private val allColors = listOf("Black", "White", "Red", "Blue", "Green")
private val allCategories = listOf("Dresses", "Shirts", "T-Shirts", "Jeans", "Shoes", "Accessories")

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    filters: ProductFilters,
    onApply: (ProductFilters) -> Unit,
    onDismiss: () -> Unit,
) {
    var draft by remember(filters) { mutableStateOf(filters) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleLarge)

            FilterSection("Category") {
                allCategories.forEach { value ->
                    FilterChip(
                        selected = value in draft.categories,
                        onClick = { draft = draft.copy(categories = draft.categories.toggle(value)) },
                        label = { Text(value) },
                    )
                }
            }
            FilterSection("Price") {
                priceRanges.forEach { range ->
                    FilterChip(
                        selected = range in draft.priceRanges,
                        onClick = { draft = draft.copy(priceRanges = draft.priceRanges.toggle(range)) },
                        label = { Text(range.label) },
                    )
                }
            }
            FilterSection("Size") {
                allSizes.forEach { value ->
                    FilterChip(
                        selected = value in draft.sizes,
                        onClick = { draft = draft.copy(sizes = draft.sizes.toggle(value)) },
                        label = { Text(value) },
                    )
                }
            }
            FilterSection("Color") {
                allColors.forEach { value ->
                    FilterChip(
                        selected = value in draft.colors,
                        onClick = { draft = draft.copy(colors = draft.colors.toggle(value)) },
                        label = { Text(value) },
                    )
                }
            }
            FilterSection("Rating") {
                listOf(4f to "4+ stars", 3f to "3+ stars").forEach { (value, label) ->
                    FilterChip(
                        selected = draft.minRating == value,
                        onClick = { draft = draft.copy(minRating = if (draft.minRating == value) null else value) },
                        label = { Text(label) },
                    )
                }
            }

            Divider()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { draft = ProductFilters() }, modifier = Modifier.weight(1f)) { Text("Clear All") }
                PrimaryButton("Apply", onClick = { onApply(draft) }, modifier = Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { content() }
    }
}

private fun <T> Set<T>.toggle(value: T): Set<T> = if (value in this) this - value else this + value
