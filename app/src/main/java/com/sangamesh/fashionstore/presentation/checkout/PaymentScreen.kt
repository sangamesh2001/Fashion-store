package com.sangamesh.fashionstore.presentation.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.presentation.common.PrimaryButton
import com.sangamesh.fashionstore.presentation.common.SecondaryButton
import com.sangamesh.fashionstore.ui.theme.Sale

@Composable
fun PaymentScreen(
    onSuccess: (orderId: Long, orderNumber: String) -> Unit,
    onCancel: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel(),
) {
    val paymentState by viewModel.paymentState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.pay() }
    LaunchedEffect(paymentState) {
        (paymentState as? PaymentUiState.Success)?.let { onSuccess(it.orderId, it.orderNumber) }
    }

    Scaffold { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (paymentState) {
                PaymentUiState.Idle, PaymentUiState.Processing -> {
                    CircularProgressIndicator(Modifier.size(56.dp))
                    Text("Processing payment…", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 20.dp))
                    Text(
                        "Please don't close the app",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                is PaymentUiState.Success -> {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Sale, modifier = Modifier.size(64.dp))
                    Text("Payment Successful", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp))
                }
                PaymentUiState.Failed -> {
                    Icon(Icons.Default.Cancel, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(64.dp))
                    Text("Payment Failed", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 16.dp, bottom = 24.dp))
                    PrimaryButton("Try Again", onClick = { viewModel.retryPayment(); viewModel.pay() })
                    SecondaryButton("Cancel", onClick = onCancel, modifier = Modifier.padding(top = 12.dp))
                }
            }
        }
    }
}
