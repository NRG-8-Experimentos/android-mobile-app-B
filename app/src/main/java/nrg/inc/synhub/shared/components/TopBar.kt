package com.example.synhub.shared.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    function: () -> Unit,
    title: String,
    icon: ImageVector,
    actions: (@Composable () -> Unit)? = null
){
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = { Text(text = title, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = function) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        actions = { actions?.invoke() }
    )
}
