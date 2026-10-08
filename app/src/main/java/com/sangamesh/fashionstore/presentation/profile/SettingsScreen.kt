package com.sangamesh.fashionstore.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val darkMode by viewModel.darkMode.collectAsStateWithLifecycle()
    val notifications by viewModel.notificationsEnabled.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Card {
                ListItem(
                    headlineContent = { Text("Dark Mode") },
                    trailingContent = { Switch(checked = darkMode, onCheckedChange = viewModel::setDarkMode) },
                    modifier = Modifier.fillMaxWidth(),
                )
                ListItem(
                    headlineContent = { Text("Notifications") },
                    trailingContent = { Switch(checked = notifications, onCheckedChange = viewModel::setNotifications) },
                    modifier = Modifier.fillMaxWidth(),
                )
                ListItem(headlineContent = { Text("Language") }, supportingContent = { Text("English") }, modifier = Modifier.fillMaxWidth())
            }
            Card(Modifier.padding(top = 16.dp)) {
                ListItem(headlineContent = { Text("About Fashion Store") }, supportingContent = { Text("Version 1.0.0") }, modifier = Modifier.fillMaxWidth())
                ListItem(headlineContent = { Text("Privacy Policy") }, modifier = Modifier.fillMaxWidth())
                ListItem(headlineContent = { Text("Terms and Conditions") }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
