package com.sangamesh.fashionstore.presentation.checkout

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
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.domain.model.PaymentMethod
import com.sangamesh.fashionstore.domain.usecase.CheckoutValidation
import com.sangamesh.fashionstore.presentation.common.PrimaryButton
import com.sangamesh.fashionstore.util.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onChangeAddress: () -> Unit,
    onProceedToPayment: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
                SectionCard("Delivery Address") {
                    if (state.selectedAddress == null) {
                        Text("No address selected", color = MaterialTheme.colorScheme.error)
                    } else {
                        val a = state.selectedAddress!!
                        Text(a.fullName, style = MaterialTheme.typography.titleMedium)
                        Text(a.phoneNumber, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(a.formatted())
                    }
                    TextButton(onClick = onChangeAddress) { Text("Change Address") }
                }

                SectionCard("Products") {
                    state.items.forEach { item ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${item.productName} × ${item.quantity}", modifier = Modifier.weight(1f))
                            Text(formatPrice(item.price * item.quantity))
                        }
                    }
                }

                SectionCard("Price Details") {
                    PriceLine("Subtotal", state.totals.subtotal)
                    if (state.totals.discount > 0) PriceLine("Discount", -state.totals.discount)
                    PriceLine("Delivery Fee", state.totals.deliveryFee)
                    Divider(Modifier.padding(vertical = 6.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", style = MaterialTheme.typography.titleMedium)
                        Text(formatPrice(state.totals.total), style = MaterialTheme.typography.titleMedium)
                    }
                }

                SectionCard("Payment Method") {
                    PaymentMethod.entries.forEach { method ->
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(selected = state.paymentMethod == method, onClick = { viewModel.selectPaymentMethod(method) })
                            Text(method.displayName)
                        }
                    }
                }
            }

            Column(Modifier.padding(16.dp)) {
                if (state.validationError != null) {
                    Text(state.validationError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
                }
                PrimaryButton(
                    "Continue to Payment",
                    onClick = {
                        when (viewModel.validate()) {
                            is CheckoutValidation.Valid -> onProceedToPayment()
                            is CheckoutValidation.Invalid -> {}
                        }
                    },
                    enabled = viewModel.validate() is CheckoutValidation.Valid,
                )
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
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
