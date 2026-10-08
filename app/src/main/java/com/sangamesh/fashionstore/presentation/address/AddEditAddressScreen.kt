package com.sangamesh.fashionstore.presentation.address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sangamesh.fashionstore.domain.model.Address
import com.sangamesh.fashionstore.presentation.common.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAddressScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AddressViewModel = hiltViewModel(),
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var line1 by remember { mutableStateOf("") }
    var line2 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("India") }
    var isDefault by remember { mutableStateOf(false) }

    val isValid = fullName.isNotBlank() && phone.isNotBlank() && line1.isNotBlank() &&
        city.isNotBlank() && state.isNotBlank() && postalCode.isNotBlank() && country.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Address") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            Field("Full Name", fullName) { fullName = it }
            Field("Phone Number", phone) { phone = it }
            Field("Address Line 1", line1) { line1 = it }
            Field("Address Line 2 (optional)", line2) { line2 = it }
            Field("City", city) { city = it }
            Field("State", state) { state = it }
            Field("Postal Code", postalCode) { postalCode = it }
            Field("Country", country) { country = it }

            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isDefault, onCheckedChange = { isDefault = it })
                Text("Set as default address")
            }

            PrimaryButton(
                "Save Address",
                enabled = isValid,
                onClick = {
                    viewModel.save(
                        Address(
                            fullName = fullName, phoneNumber = phone, addressLine1 = line1, addressLine2 = line2,
                            city = city, state = state, postalCode = postalCode, country = country, isDefault = isDefault,
                        ),
                        onSaved = onSaved,
                    )
                },
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

@Composable
private fun Field(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, label = { Text(label) },
        singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
    )
}
