package com.sangamesh.fashionstore.presentation.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sangamesh.fashionstore.presentation.common.PrimaryButton
import com.sangamesh.fashionstore.presentation.common.SecondaryButton
import androidx.compose.material3.Icon
import com.sangamesh.fashionstore.ui.theme.Sale

@Composable
fun OrderSuccessScreen(
    orderNumber: String,
    onViewOrder: () -> Unit,
    onContinueShopping: () -> Unit,
) {
    Scaffold { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Sale, modifier = Modifier.size(72.dp))
            Text("Order Placed Successfully", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 16.dp))
            Text("Order #$orderNumber", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
            Text(
                "Estimated Delivery: 2–5 business days",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp, bottom = 32.dp),
            )
            PrimaryButton("View Order", onClick = onViewOrder)
            SecondaryButton("Continue Shopping", onClick = onContinueShopping, modifier = Modifier.padding(top = 12.dp))
        }
    }
}
