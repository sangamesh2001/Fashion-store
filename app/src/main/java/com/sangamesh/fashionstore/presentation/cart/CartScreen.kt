package com.sangamesh.fashionstore.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sangamesh.fashionstore.domain.model.CartItem
import com.sangamesh.fashionstore.presentation.common.EmptyView
import com.sangamesh.fashionstore.presentation.common.PriceView
import com.sangamesh.fashionstore.presentation.common.PrimaryButton
import com.sangamesh.fashionstore.presentation.common.QuantitySelector
import com.sangamesh.fashionstore.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onCheckout: () -> Unit,
    onBrowse: () -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Cart") }) }) { padding ->
        if (state.items.isEmpty()) {
            Column(Modifier.padding(padding).fillMaxSize()) {
                EmptyView("Your cart is empty", "Browse products and add your favorites.")
                PrimaryButton("Continue Shopping", onClick = onBrowse, modifier = Modifier.padding(24.dp))
            }
            return@Scaffold
        }

        Column(Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(Modifier.weight(1f), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
                items(state.items, key = { it.id }) { item ->
                    CartRow(
                        item,
                        onIncrement = { viewModel.increment(item) },
                        onDecrement = { viewModel.decrement(item) },
                        onRemove = { viewModel.remove(item) },
                    )
                }
            }

            Card(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)) {
                Column(Modifier.padding(20.dp)) {
                    PriceRow("Subtotal", state.totals.subtotal)
                    if (state.totals.discount > 0) PriceRow("Discount", -state.totals.discount, negative = true)
                    PriceRow("Delivery Fee", state.totals.deliveryFee)
                    Divider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", style = MaterialTheme.typography.titleMedium)
                        Text(formatPrice(state.totals.total), style = MaterialTheme.typography.titleMedium)
                    }
                    PrimaryButton("Proceed to Checkout", onClick = onCheckout, modifier = Modifier.padding(top = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, amount: Int, negative: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            (if (negative) "-" else "") + formatPrice(kotlin.math.abs(amount)),
            color = if (negative) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun CartRow(item: CartItem, onIncrement: () -> Unit, onDecrement: () -> Unit, onRemove: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.productName,
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.size(84.dp).clip(RoundedCornerShape(12.dp)),
        )
        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(item.productName, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row {
                item.size?.let { Text("Size: $it  ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                item.color?.let { Text("Color: $it", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            PriceView(item.price, item.originalPrice, if (item.originalPrice > item.price) (item.originalPrice - item.price) * 100 / item.originalPrice else 0, modifier = Modifier.padding(top = 4.dp))
            Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                QuantitySelector(item.quantity, onIncrement, onDecrement, maxQuantity = item.stock.coerceAtLeast(1))
            }
        }
        IconButton(onClick = onRemove) { Icon(Icons.Default.Delete, contentDescription = "Remove item") }
    }
}
