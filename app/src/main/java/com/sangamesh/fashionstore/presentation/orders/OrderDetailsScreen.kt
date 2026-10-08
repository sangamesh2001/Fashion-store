package com.sangamesh.fashionstore.presentation.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sangamesh.fashionstore.domain.model.Order
import com.sangamesh.fashionstore.domain.model.OrderStatus
import com.sangamesh.fashionstore.presentation.common.LoadingView
import com.sangamesh.fashionstore.util.formatPrice

private val trackedStatuses = listOf(
    OrderStatus.PLACED, OrderStatus.CONFIRMED, OrderStatus.SHIPPED, OrderStatus.DELIVERED,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    onBack: () -> Unit,
    viewModel: OrderDetailsViewModel = hiltViewModel(),
) {
    val order by viewModel.order.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            )
        },
    ) { padding ->
        val current = order
        if (current == null) {
            LoadingView(Modifier.padding(padding))
            return@Scaffold
        }
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("Order #${current.orderNumber}", style = MaterialTheme.typography.titleLarge)

            SectionCard("Order Status") { StatusTimeline(current.orderStatus) }

            SectionCard("Products") {
                current.items.forEach { item ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        AsyncImage(
                            model = item.imageUrl, contentDescription = item.productName,
                            contentScale = ContentScale.Crop,
                            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)),
                        )
                        Column(Modifier.padding(start = 10.dp).weight(1f)) {
                            Text(item.productName, style = MaterialTheme.typography.bodyMedium)
                            val variant = listOfNotNull(item.size?.let { "Size $it" }, item.color).joinToString(" · ")
                            if (variant.isNotBlank()) {
                                Text(variant, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("Qty ${item.quantity}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(formatPrice(item.price * item.quantity))
                    }
                }
            }

            SectionCard("Delivery Address") {
                Text(current.address.fullName, style = MaterialTheme.typography.titleMedium)
                Text(current.address.phoneNumber, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(current.address.formatted())
            }

            SectionCard("Payment") {
                Text("Method: ${current.paymentMethod.displayName}")
                Text("Status: ${current.paymentStatus.name.lowercase().replaceFirstChar { it.uppercase() }}")
            }

            SectionCard("Total") {
                PriceLine("Subtotal", current.subtotal)
                if (current.discount > 0) PriceLine("Discount", -current.discount)
                PriceLine("Delivery Fee", current.deliveryFee)
                Divider(Modifier.padding(vertical = 6.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", style = MaterialTheme.typography.titleMedium)
                    Text(formatPrice(current.totalAmount), style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun StatusTimeline(currentStatus: OrderStatus) {
    val currentIndex = trackedStatuses.indexOf(currentStatus).let { if (it == -1) 0 else it }
    Column {
        trackedStatuses.forEachIndexed { index, status ->
            val reached = index <= currentIndex
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                androidx.compose.foundation.layout.Box(
                    Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background0(if (reached) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    if (reached) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
                Text(
                    status.label,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (reached) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun Modifier.background0(color: Color) = this.then(Modifier.background(color))

@Composable
private fun SectionCard(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            content()
        }
    }
}

@Composable
private fun PriceLine(label: String, amount: Int) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text((if (amount < 0) "-" else "") + formatPrice(kotlin.math.abs(amount)))
    }
}
