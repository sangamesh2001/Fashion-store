package com.sangamesh.fashionstore.presentation.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.presentation.common.EmptyView

/**
 * @param selectionMode when true, tapping a card selects it for checkout via onSelect
 *   instead of just editing; used when this screen is opened from Checkout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    onBack: () -> Unit,
    onAddNew: () -> Unit,
    onEdit: (Long) -> Unit,
    selectionMode: Boolean = false,
    onSelect: (Address) -> Unit = {},
    viewModel: AddressViewModel = hiltViewModel(),
) {
    val addresses by viewModel.addresses.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectionMode) "Select Address" else "Addresses") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            )
        },
        floatingActionButton = { FloatingActionButton(onClick = onAddNew) { Icon(Icons.Default.Add, "Add address") } },
    ) { padding ->
        if (addresses.isEmpty()) {
            EmptyView("No saved addresses", "Add a delivery address to check out faster.", modifier = Modifier.padding(padding).fillMaxSize())
            return@Scaffold
        }
        LazyColumn(Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(addresses, key = { it.id }) { address ->
                Card(
                    onClick = { if (selectionMode) onSelect(address) else onEdit(address.id) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (selectionMode) {
                            RadioButton(selected = address.isDefault, onClick = { onSelect(address) })
                        }
                        Column(Modifier.weight(1f)) {
                            Row {
                                Text(address.fullName, style = MaterialTheme.typography.titleMedium)
                                if (address.isDefault) {
                                    Text(
                                        "  · Default",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                            Text(address.phoneNumber, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(address.formatted(), style = MaterialTheme.typography.bodyMedium)
                        }
                        if (!selectionMode) {
                            IconButton(onClick = { viewModel.delete(address.id) }) { Icon(Icons.Default.Delete, "Delete address") }
                        }
                    }
                }
            }
        }
    }
}
